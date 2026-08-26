Exercício 1: Aplicações
Para cada cenário abaixo, indique se o padrão Abstract Factory é apropriado ou não e justifique em 2–3 frases.

Uma aplicação de interface gráfica que precisa trocar o tema/plataforma (Windows, macOS, Linux). Cada plataforma exige uma família coerente de componentes (botão, checkbox, janela), e misturar componentes de plataformas diferentes quebra a interface.
R: sim

Uma classe simples Ponto com apenas dois campos obrigatórios (x, y), utilizada em um sistema de CAD e instanciada dezenas de vezes por segundo através do construtor tradicional.
Não,
Um módulo de acesso a bancos de dados que precisa manter famílias consistentes por fornecedor: para MySQL existem ConexaoMySQL, ComandoMySQL e TransacaoMySQL; para PostgreSQL existem as versões PostgreSQL. Todos os objetos usados juntos devem vir do mesmo fornecedor.
Sim,
Uma loja que vende kits de móveis por estilo (moderno, vitoriano, art déco). Cada kit é composto por cadeira + sofá + mesa de centro e o cliente espera que os três combinem entre si.

Uma classe Produto com três campos obrigatórios (nome, preco, quantidadeEstoque), criada em um único ponto do sistema através do construtor tradicional e sem variações.

Para cada item, responda:
“Faz sentido usar Abstract Factory” ou “Não faz sentido usar Abstract Factory”.
Explique rapidamente o porquê (coerência de famílias, acoplamento, troca de variantes, overengineering, etc.).

Exercício 2: Analogia
Crie uma analogia própria para explicar o padrão Abstract Factory para alguém que não é da área de TI.

Na aula, usamos a coleção de roupas: uma coleção de verão tem peças que combinam entre si, e misturar verão + inverno no mesmo look “quebra” a harmonia. Agora crie uma outra analogia.

Descreva uma situação do mundo real em que:

existam famílias de itens que devem ser usadas juntas (coerência),
existam variantes diferentes da mesma família,
e misturar itens de famílias/variantes diferentes gere um resultado incoerente.

Exercício 3: Anti-pattern
Considere o código Java abaixo, usado para montar a interface gráfica de uma aplicação:

public class Aplicacao {

    private Botao botao;
    private Checkbox checkbox;

    public Aplicacao(String sistemaOperacional) {
        if (sistemaOperacional.equals("windows")) {
            botao = new BotaoWindows();
            checkbox = new CheckboxWindows();
        } else if (sistemaOperacional.equals("linux")) {
            botao = new BotaoLinux();
            checkbox = new CheckboxWindows();
        }
    }

    public void exibir() {
        botao.renderizar();
        checkbox.alternar();
    }
}

Responda:

Por que essa forma de criar os componentes com new é um problema de design?
Que tipo de bug ou comportamento estranho pode acontecer quando o código mistura componentes de famílias diferentes (como no linux)? E o que aconteceria ao adicionar um novo sistema operacional (ex.: mac)?
Proponha uma solução usando o padrão Abstract Factory, explicando em linhas gerais: os produtos abstratos, a fábrica abstrata, as fábricas concretas por SO e como o cliente (a Aplicacao) passaria a receber a fábrica.
Exercício 4: Exemplo real
Acesse os seguintes arquivos em um projeto open source:

Projeto: iluwatar/java-design-patterns (um dos repositórios de padrões de projeto mais populares do GitHub)
Arquivos:
KingdomFactory.java
ElfKingdomFactory.java
Esse exemplo implementa o Abstract Factory com reinos (kingdoms): a interface KingdomFactory declara os métodos de criação de uma família de produtos (createCastle, createKing, createArmy), e cada fábrica concreta (como o ElfKingdomFactory, ou o OrcKingdomFactory do mesmo pacote) cria uma família coerente. É a mesma estrutura vista em aula com a FabricaMobilia.

Responda:

Que família de produtos a interface KingdomFactory produz? Liste os métodos de criação.
No ElfKingdomFactory, que objetos concretos são criados por cada método? O que aconteceria se um desses métodos retornasse, por engano, um produto orc (por exemplo, OrcCastle)? (coerência da família)
O cliente do exemplo (a classe App do mesmo pacote) monta o reino recebendo uma fábrica. O que precisaria mudar no código do cliente para trocar de reino elfo para orc? Relacione com o princípio OCP.