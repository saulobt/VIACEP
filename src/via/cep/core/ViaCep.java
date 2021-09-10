/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package via.cep.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author saulo
 */
public class ViaCep {

    public static final String JSON = "json";
    public static final String XML = "xml";
    public static final String PIPED = "piped";
    private static String tipoRetorno = JSON;
    private static String cep;
    private static String urlCep;
    private static StringBuffer result;

    public static String getCep(String CEP, String TipoRetorno) {
        cep = CEP;
        tipoRetorno = TipoRetorno;
        requestURL();
        return result.toString();
    }

    public static Endereco getCep(String CEP) {
        cep = CEP;
        tipoRetorno = PIPED;
        requestURL();
        String[] endereco = result.toString().split("[|]");
        System.out.println("Result: " + result.toString());
        Endereco e = new Endereco();
        try {
            e.setCep(getFild(endereco[0]));//cep
            e.setLogradouro(getFild(endereco[1]));//logradouro
            e.setComplemento(getFild(endereco[2]));//complemento
            e.setBairro(getFild(endereco[3]));//bairro
            e.setCidade(getFild(endereco[4]));//localidade
            e.setUf(getFild(endereco[5]));//uf
            e.setUnidade(getFild(endereco[6]));//unidade
            e.setCodigoIBGE(getFild(endereco[7]));//ibge
            e.setGia(getFild(endereco[8]));//gia
        } catch (Exception ex) {
            e.setLogradouro("CEP: " + cep + " Não encontrado...");//logradouro
            return e;
        }
        return e;
    }

    private static void requestURL() {
        URL link;
        String inputLine;
        try {
            result = new StringBuffer();
            urlCep = "https://viacep.com.br/ws/" + cep + "/" + tipoRetorno + "/ "; // link original: viacep.com.br/ws/01001000/json/
            link = new URL(urlCep);
            System.out.println("Acessando ViaCep: " + urlCep);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(link.openStream()));
            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                result.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException ex) {
            result.append("MalformedURLException ERRO: " + ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            result.append("IOException ERRO: " + ex);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // System.out.println(ViaCep.getCep("39404153", ViaCep.PIPED));
        Endereco e = ViaCep.getCep("39404153");
        System.out.println(e.getLogradouro() + ", " + e.getComplemento() + " " + e.getBairro() + " - " + e.getCidade() + "/" + e.getUf());
        System.out.println("usando git versionamento");
    }

    private static String getFild(String dados) {
        // System.out.println("dados:" + dados);
        String[] campo = dados.split(":");
        try {
            return new String(campo[1]);
        } catch (Exception e) {
            return "";
        }
    }

}
