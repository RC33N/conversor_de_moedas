import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ConversorDeMoedas {

    private static final String API_KEY = "04a9e6ccff696a0e63263272"; // ← Substitua pela sua chave
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";
    private static final String ARQUIVO_HISTORICO = "historico.txt";

    static class ExchangeResponse {
        String result;

        @SerializedName("base_code")
        String baseCode;

        @SerializedName("conversion_rates")
        Map<String, Double> conversionRates;
    }

    public static ExchangeResponse obterTaxas(String moedaBase) throws Exception {
        String url = API_URL + moedaBase;
        HttpURLConnection conexao = (HttpURLConnection) new URL(url).openConnection();
        conexao.setRequestMethod("GET");

        if (conexao.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Erro na conexão: " + conexao.getResponseCode());
        }

        try (InputStreamReader reader = new InputStreamReader(conexao.getInputStream())) {
            return new Gson().fromJson(reader, ExchangeResponse.class);
        }
    }

    public static double converter(String origem, String destino, double valor) throws Exception {
        ExchangeResponse resposta = obterTaxas(origem);

        if (!"success".equalsIgnoreCase(resposta.result)) {
            throw new RuntimeException("Erro ao obter taxas de câmbio.");
        }

        Double taxa = resposta.conversionRates.get(destino);

        if (taxa == null) {
            throw new RuntimeException("Moeda de destino não encontrada.");
        }

        return valor * taxa;
    }

    // Menu que o usuário verá ao iniciar o programa

    public static void exibirMenu() {
        System.out.println("\n===== CONVERSOR DE MOEDAS =====");
        System.out.println("1. Real (BRL) para Dólar (USD)");
        System.out.println("2. Dólar (USD) para Real (BRL)");
        System.out.println("3. Euro (EUR) para Real (BRL)");
        System.out.println("4. Real (BRL) para Euro (EUR)");
        System.out.println("5. Real (BRL) para Peso Argentino (ARS)");
        System.out.println("6. Dólar (USD) para Euro (EUR)");
        System.out.println("7. Ver histórico de conversões");
        System.out.println("8. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public static void salvarHistorico(String entrada) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_HISTORICO, true))) {
            writer.write(entrada);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    // Metodo para mostrar o histórico de conversões

    public static void mostrarHistorico() {
        System.out.println("\n=== Histórico de Conversões ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_HISTORICO))) {
            String linha;
            boolean vazio = true;
            while ((linha = reader.readLine()) != null) {
                System.out.println(linha);
                vazio = false;
            }
            if (vazio) {
                System.out.println("Nenhuma conversão realizada ainda.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o histórico: " + e.getMessage());
        }
    }
    // Metodo para o usuario escolher a moeda de origem e destino, e realizar a conversão

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();
            opcao = scanner.nextInt();

            String origem = "";
            String destino = "";

            switch (opcao) {
                case 1:
                    origem = "BRL";
                    destino = "USD";
                    break;
                case 2:
                    origem = "USD";
                    destino = "BRL";
                    break;
                case 3:
                    origem = "EUR";
                    destino = "BRL";
                    break;
                case 4:
                    origem = "BRL";
                    destino = "EUR";
                    break;
                case 5:
                    origem = "BRL";
                    destino = "ARS";
                    break;
                case 6:
                    origem = "USD";
                    destino = "EUR";
                    break;
                case 7:
                    mostrarHistorico();
                    continue; // volta para o menu sem pedir valor
                case 8:
                    System.out.println("\nVocê escolheu sair. Aqui está o histórico de conversões realizadas:");
                    mostrarHistorico();
                    System.out.println("\nEncerrando o programa...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    continue;
            }

            if (opcao <= 6) {
                System.out.print("Digite o valor para conversão: ");
                double valor = scanner.nextDouble();

                try {
                    double convertido = converter(origem, destino, valor);
                    String resultado = String.format("Resultado: %.2f %s = %.2f %s", valor, origem, convertido, destino);
                    System.out.println(resultado);
                    salvarHistorico(resultado);
                } catch (Exception e) {
                    System.out.println("Erro ao converter: " + e.getMessage());
                }
            }
            // Enquandto o usuário não escolher a opção de sair, o menu será exibido novamente
        } while (opcao != 8);

        scanner.close();
    }
}
