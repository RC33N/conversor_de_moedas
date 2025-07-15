<img width="204" height="192" alt="download" src="https://github.com/user-attachments/assets/4b8b3ec0-bfe6-4a0a-8620-b7723adb6820" />

Este projeto em java é um simples conversor de moedas em tempo real com busca na internet utilizando a API EXCHANGERATE

🚀 Funcionalidades
📡 Acessa a internet para obter taxas de câmbio atualizadas

📊 Converte entre moedas como BRL, USD, EUR, ARS

💾 Armazena cada conversão realizada em historico.txt

📋 Mostra o histórico de conversões ao usuário antes de encerrar

Como é possivel ver ao analizar o codigo é notavel que o usuario é capaz de visualizar seu historico de conversoes
esse é um dos diferenciais deste projeto, e como que ela funciona? 

Cada vez que o usuário realiza uma conversão no programa, o resultado é armazenado em um arquivo chamado historico.txt. Esse arquivo é mantido no mesmo diretório onde o programa é executado. As informações são gravadas linha por linha, e podem ser lidas a qualquer momento.

PRIMEIRA ETAPA

No momento em que uma conversão é feita com sucesso, o programa executa este método:
public static void salvarHistorico(String entrada) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_HISTORICO, true))) {
        writer.write(entrada);
        writer.newLine();
    } catch (IOException e) {
        System.out.println("Erro ao salvar histórico: " + e.getMessage());
    }
}

📚 Etapas técnicas da leitura:
Quando o usuário escolhe opção 7 (Ver histórico) ou opção 8 (Sair), o programa executa:
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


📖 Explicando:
BufferedReader é usado para ler o arquivo linha por linha.

Cada linha é impressa na tela.

Um booleano vazio é usado para detectar se o arquivo está vazio (sem conversões ainda).


