Atividade 01: SOLID
Estes exercícios são destinados a praticar os conceitos introdutórios de SOLID. Certifique-se de entender cada princípio antes de tentar resolver os exercícios. Não utilize recursos externos para resolver os exercícios, pois o objetivo é desenvolver sua compreensão dos princípios SOLID por meio da prática.

Exercício 1 – Single Responsibility Principle (SRP)
Observe a classe RelatorioVendas abaixo:

public class RelatorioVendas {

    private final List<Venda> vendas;
    private final LocalDate dataInicio;
    private final LocalDate dataFim;

    public RelatorioVendas(List<Venda> vendas, LocalDate dataInicio, LocalDate dataFim) {
        this.vendas = vendas;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatório de Vendas\n");
        sb.append("Período: ").append(dataInicio).append(" até ").append(dataFim).append("\n\n");

        double total = 0.0;
        for (Venda venda : vendas) {
            sb.append("ID: ").append(venda.getId())
              .append(" | Data: ").append(venda.getData())
              .append(" | Cliente: ").append(venda.getCliente())
              .append(" | Valor: R$ ").append(String.format("%.2f", venda.getValor()))
              .append("\n");
            total += venda.getValor();
        }

        sb.append("\nTotal de vendas: R$ ").append(String.format("%.2f", total)).append("\n");
        return sb.toString();
    }

    public void salvarRelatorio(String caminho) {
        String conteudo = gerarRelatorio();
        try (FileWriter writer = new FileWriter(caminho)) {
            writer.write(conteudo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar relatório em arquivo: " + caminho, e);
        }
    }

    public void enviarRelatorioPorEmail(String email) {
        String conteudo = gerarRelatorio();
        EmailService emailService = new EmailService();
        emailService.enviarEmail(email, "Relatório de Vendas", conteudo);
    }

    public static class Venda {
        private final String id;
        private final LocalDate data;
        private final String cliente;
        private final double valor;

        public Venda(String id, LocalDate data, String cliente, double valor) {
            this.id = id;
            this.data = data;
            this.cliente = cliente;
            this.valor = valor;
        }

        public String getId() {
            return id;
        }

        public LocalDate getData() {
            return data;
        }

        public String getCliente() {
            return cliente;
        }

        public double getValor() {
            return valor;
        }
    }
}

Explique por que essa classe viola o SRP.
Liste pelo menos três responsabilidades diferentes que ela está assumindo.
Proponha uma nova estrutura de classes usando um diagrama de classes que distribua essas responsabilidades de forma mais adequada.


Exercício 2 – Open/Closed Principle (OCP)
Imagine um sistema de cálculo de frete onde existe uma classe CalculadoraFrete com um switch/if-else interno que decide o valor com base no tipo de entrega: Normal, Rápida, Expressa.



CalculadoraFrete.java
public class CalculadoraFrete {

    public double calcularFrete(String tipoEntrega, double peso) {
        switch (tipoEntrega) {
            case "Normal":
                return peso * 5.0;
            case "Rápida":
                return peso * 10.0;
            case "Expressa":
                return peso * 20.0;
            default:
                throw new IllegalArgumentException("Tipo de entrega desconhecido: " + tipoEntrega);
        }
    }
}

A empresa deseja adicionar novos tipos de entrega (por exemplo, Entrega Noturna, Entrega Internacional) sem precisar modificar o código existente.

Explique quais problemas o código atual apresenta em relação ao OCP.
Proponha uma implementação criando uma interface chamada TipoEntrega e classes concretas para cada tipo de entrega, de forma que cada tipo de entrega tenha um método calcularFrete(double peso).
Exercício 3 – Liskov Substitution Principle (LSP)
Considere uma hierarquia de classes onde Conta é a classe base e ContaCorrente, ContaPoupanca e ContaSalario são subclasses.

A classe Conta possui os seguintes métodos:

sacar(Double valor): permite sacar o valor informado da conta, desde que haja saldo suficiente.
depositar(Double valor): permite depositar dinheiro na conta.
transferir(Double valor, Conta conta): permite transferir o valor informado para outra conta.
No entanto, devido a uma regra específica do sistema, a classe ContaSalario sobrescreve os métodos sacar e transferir. Nessa classe, essas operações não realizam a operação solicitada diretamente: o valor movimentado é automaticamente direcionado para uma conta específica vinculada ao empregador.

Explique, com suas palavras, quando uma subclasse viola o Princípio da Substituição de Liskov (LSP).

Descreva um cenário de uso em que ContaSalario poderia causar um comportamento inesperado ou incorreto ao ser utilizada onde o sistema esperava receber uma Conta genérica.

Explique por que o problema apresentado está relacionado ao comportamento esperado da classe, e não simplesmente à existência dos mesmos métodos na classe filha.

Exercício 4 – Interface Segregation Principle (ISP)
Suponha uma interface ITrabalhador com os métodos: trabalhar(), comer(), dormir(). Ela é implementada por Robo e Funcionario.

Explique por que essa interface pode violar o ISP.
Proponha um conjunto de interfaces menores que segregue melhor as responsabilidades.
Escreva a nova definição dessas interfaces em pseudocódigo (ou sintaxe de alguma linguagem OO) e indique quais seriam implementadas por Robo e por Funcionario.
Exercício 5 – Dependency Inversion Principle (DIP)
Um módulo de alto nível ProcessadorDePagamento instancia diretamente uma classe concreta PagInseguro usando new e chama seus métodos em todas os métodos (ex.: processarPagamento(), verificarPagamento(), cancelarPagamento()).

Explique por que isso fere o DIP.
Quais seriam as consequências de manter essa dependência direta em termos de manutenção e evolução do sistema?
Descreva brevemente por quais maneiras o desenvolvedor poderia injetar a implementação concreta.