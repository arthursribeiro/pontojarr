package com.googlecode.imheresi1.acceptancetests;

import java.util.ArrayList;
import java.util.List;

import easyaccept.EasyAcceptFacade;

public class Teste {

    public static void main(String[] args) throws Exception {
        List<String> files = new ArrayList<String>();

        files.add("testes_aceitacao/us1.txt");
        files.add("testes_aceitacao/us2.txt");
        files.add("testes_aceitacao/us3.txt");
        files.add("testes_aceitacao/us4.txt");
        files.add("testes_aceitacao/us5.txt");
        files.add("testes_aceitacao/us6.txt");
        files.add("testes_aceitacao/us7_1.txt");
        files.add("testes_aceitacao/us7_2.txt");
        files.add("testes_aceitacao/us7_3.txt");
       
        SystemFacade facade = new SystemFacade();

        EasyAcceptFacade eaFacade = new EasyAcceptFacade(facade, files);

        eaFacade.executeTests();

        System.out.println(eaFacade.getCompleteResults());

    }

}
