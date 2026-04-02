package com.ProjetoPcamargo.projeto_exemplo.exceptions;

public class RecursoNaoEcontrado extends RuntimeException{

    // aqui ele ta pegando a mensagem que vamos colocar e setando no super do metodo pai la
    // extendido de runtime exception, sendo assim ja tendo uma mensagem pronta ou
    // nos possibilitando colocar tambem
    public RecursoNaoEcontrado(String message){
        super(message);
    }
}
