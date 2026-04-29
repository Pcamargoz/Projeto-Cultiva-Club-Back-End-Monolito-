# syntax=docker/dockerfile:1.7
# ============================================================
# CultivaClub - Monólito · Dockerfile multi-stage
# ------------------------------------------------------------
# Build:   Maven 3.9 + Eclipse Temurin JDK 21 (alinha com
#          <java.version>21</java.version> do pom.xml)
# Runtime: apenas JRE 21 (imagem final menor, sem maven nem
#          ferramentas de build)
# Render:  Render injeta a env var $PORT para todo Web Service.
#          O ENTRYPOINT propaga via --server.port=${PORT:-...}
#          então Spring Boot escuta na porta certa em prod.
# ============================================================

# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# 1) Copia primeiro só o pom.xml e baixa dependências.
#    Isso preserva o cache de dependências entre rebuilds quando
#    apenas o código-fonte muda (sem alterar o pom).
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline -DskipTests

# 2) Copia o código e empacota o jar final.
#    -DskipTests porque os testes de integração dependem de um
#    Postgres real (Supabase) e não devem rodar dentro do build.
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests \
 && cp target/cultivaclub-monolito-*.jar app.jar

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Usuário não-root: boa prática para qualquer container em produção.
# 1001 evita conflito com os UIDs reservados do Ubuntu/Debian.
RUN groupadd --system --gid 1001 spring \
 && useradd  --system --uid 1001 --gid spring --shell /usr/sbin/nologin spring

# Copia o jar do stage de build (já renomeado para app.jar).
COPY --from=build --chown=spring:spring /workspace/app.jar /app/app.jar

USER spring

# Porta default local. Em produção no Render, $PORT (injetado
# pela plataforma) tem precedência via ENTRYPOINT abaixo.
ENV SERVER_PORT=8080
EXPOSE 8080

# JVM tuning para containers:
#  - MaxRAMPercentage=75: a JVM usa até 75% da RAM disponível
#    no container (essencial em planos free do Render: 512MB).
#  - ExitOnOutOfMemoryError: container reinicia rapidamente em
#    caso de OOM em vez de ficar em estado degradado.
#  - egd=urandom: acelera startup do SecureRandom em containers.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom"

# Forma shell para expandir $PORT (Render) e $JAVA_OPTS.
# `exec` substitui o shell pelo processo java, garantindo que
# o Spring receba SIGTERM corretamente em shutdown gracioso.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar --server.port=${PORT:-${SERVER_PORT:-8080}}"]
