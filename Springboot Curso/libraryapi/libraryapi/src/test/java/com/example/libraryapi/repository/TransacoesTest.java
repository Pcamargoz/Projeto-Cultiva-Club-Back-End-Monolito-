package com.example.libraryapi.repository;

import com.example.libraryapi.serivce.TransacaoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransacoesTest {

    @Autowired
    TransacaoService transacoesTest;



    // indica que vai abrir uma transação no inicio da operação
    // fazendo um commit para confrimar alterações ou
    // rollback para desfazer as operações durante qualquer erro
    @Test
    void transacaoSimples(){
        // salvar o livro
        // salvar o autor
        // alugar o livro
        // enviar email pro locatario
        // notificar que o livro saiu da livraria

        transacoesTest.executar();
    }

}
