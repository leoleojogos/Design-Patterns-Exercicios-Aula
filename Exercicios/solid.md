Exercício 1 – Single Responsibility Principle (SRP)A classe viola o SRP porque ela tem mais de uma razão para mudar, não tendo apenas uma responsabilidade principal, o que diminui sua coesão. 

 Responsabilidades assumidas:Gerar o relatório com os cálculos e formatação.Salvar o relatório em um arquivo no sistema de diretórios.
 
 Enviar o relatório via e-mail.Estrutura proposta:RelatorioVendas: Responsável apenas por gerar os dados e o texto do relatório.  RelatorioRepository: Responsável exclusivamente por salvar o arquivo.  RelatorioNotificador (ou EmailService): Responsável por enviar o e-mail.  


Exercício 2 – Open/Closed Principle (OCP)O código não está fechado para modificação. Para adicionar um novo tipo de entrega, o desenvolvedor é forçado a alterar o código já existente dentro do switch, em vez de apenas estender o comportamento.  Implementação proposta:

Java public interface TipoEntrega {
    double calcularFrete(double peso);
}

public class EntregaNormal implements TipoEntrega {
    public double calcularFrete(double peso) { return peso * 5.0; }
}

public class EntregaRapida implements TipoEntrega {
    public double calcularFrete(double peso) { return peso * 10.0; }
}

public class CalculadoraFrete {
    public double calcularFrete(TipoEntrega tipoEntrega, double peso) {
        return tipoEntrega.calcularFrete(peso);
    }
}
Exercício 3 – Liskov Substitution Principle (LSP)Uma subclasse viola o LSP quando ela não pode substituir a sua superclasse sem quebrar o comportamento esperado pelo sistema.  Cenário: Um sistema bancário que percorre uma lista genérica de objetos Conta para debitar uma tarifa mensal usando sacar(). Se passar por uma ContaSalario, o valor seria transferido para o empregador em vez de debitar a tarifa, causando um rombo contábil.O problema ocorre porque o contrato da classe base é quebrado. A superclasse promete que o método sacar apenas retira dinheiro. A subclasse altera a semântica dessa operação para um redirecionamento de fundos, frustrando a expectativa da classe cliente.

Exercício 4 – Interface Segregation Principle (ISP)A interface viola o ISP porque força clientes (como a classe Robo) a dependerem de métodos (como comer() e dormir()) que não usam.  O ideal é usar interfaces pequenas e específicas: ITrabalhador, IAlimentavel e IDescansavel.  Implementação proposta:

Java public interface ITrabalhador { void trabalhar(); }
public interface IAlimentavel { void comer(); }
public interface IDescansavel { void dormir(); }

public class Funcionario implements ITrabalhador, IAlimentavel, IDescansavel {
    public void trabalhar() { }
    public void comer() { }
    public void dormir() { }
}

public class Robo implements ITrabalhador {
    public void trabalhar() { }
}
Exercício 5 – Dependency Inversion Principle (DIP)Fere o DIP porque um módulo de alto nível (ProcessadorDePagamento) está dependendo diretamente de um detalhe de implementação/módulo de baixo nível (PagInseguro), quando ambos deveriam depender de abstrações.  Consequências: Alto acoplamento. Qualquer mudança na forma de pagamento exigirá alterar diretamente a classe de alto nível, além de dificultar ou impossibilitar a criação de testes automatizados com mocks.O desenvolvedor deve criar uma abstração (uma interface, como IPagamento), fazer o PagInseguro implementá-la, e injetar essa dependência no ProcessadorDePagamento através de seu construtor. 