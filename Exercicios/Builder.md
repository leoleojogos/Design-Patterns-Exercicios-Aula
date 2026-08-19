Atividade 03: Builder
Exercício 1: Aplicações
Para cada cenário abaixo, indique se o padrão Builder é apropriado ou não e justifique em 2–3 frases.

Uma classe ConfiguracaoServidor que representa as configurações de conexão de um serviço, com campos como host, porta, timeout, quantidadeDeTentativas, usarSSL e proxy, onde a maioria dos campos é opcional e varia entre ambiente de desenvolvimento e produção.
Um componente responsável por montar requisições HTTP para chamadas a APIs externas, onde o código encadeia URL, método, cabeçalhos e corpo de forma legível (Request.Builder do OkHttp).
Uma classe simples Ponto com apenas dois campos obrigatórios (x, y), utilizada em um sistema de CAD e instanciada dezenas de vezes por segundo.
Um RelatorioFinanceiro com muitas opções de configuração: título, período, filtros, ordenação, formato de saída (PDF, CSV), marca d’água e rodapé, sendo que diferentes módulos geram combinações diferentes sem alterar o código de montagem.
Uma classe Produto com três campos obrigatórios (nome, preco, quantidadeEstoque), criada em um único ponto do sistema através do construtor tradicional.
Para cada item, responda:

“Faz sentido usar Builder” ou “Não faz sentido usar Builder”.
Explique rapidamente o porquê (quantidade de campos, opcionais, legibilidade, risco de overengineering, etc.).

R: como é um sistema simples e 2 campo não faz sentido usar builder

Exercício 2: Analogia
Crie uma analogia própria para explicar o padrão Builder para alguém que não é da área de TI.

Descreva uma situação do mundo real em que:
um objeto seja montado passo a passo (etapas bem definidas),
existam partes obrigatórias e partes opcionais,
e as mesmas etapas possam gerar variações diferentes do resultado final.
Explique por que essa analogia representa bem:
a separação entre o que é montado (Produto) e como é montado (Builder),
a ideia de montagem em passos claros,
e a possibilidade de variar combinações sem criar um novo processo do zero.
Indique também uma limitação da sua analogia (algo que não encaixa perfeitamente com o padrão).


R: Realizar um sanduiche, nessa etapa explica bem pois e simples rapido e prático, demostrando que até pessoas simples conseguem comprender o builder só que de forma diferente

Exercício 3: Anti-pattern
Considere o código Java abaixo, usado em uma aplicação de e-commerce:

public class Pedido {

    private final String cliente;
    private final String endereco;
    private final List<String> itens;
    private final double desconto;
    private final String cupom;
    private final double frete;
    private final String observacoes;

    public Pedido(String cliente) { ... }

    public Pedido(String cliente, String endereco) { ... }

    public Pedido(String cliente, String endereco, List<String> itens) { ... }

    public Pedido(String cliente, String endereco, List<String> itens, double desconto) { ... }

    public Pedido(String cliente, String endereco, List<String> itens, double desconto, String cupom) { ... }

    public Pedido(String cliente, String endereco, List<String> itens, double desconto, String cupom, double frete, String observacoes) { ... }

    // ...outros métodos de regras de negócio...
}

Responda:

Por que esse uso de construtores sobrecarregados (telescópicos) é um problema de design?
Que tipo de bugs ou confusões podem acontecer quando um desenvolvedor cria um Pedido chamando esses construtores?
O que acontece com esse código a cada novo campo opcional adicionado à Pedido? Quantos construtores seriam necessários para N campos opcionais?
Sugira outra abordagem de design usando o padrão Builder e explique, em linhas gerais, como a criação do objeto passaria a funcionar (construtor privado, Builder interno, métodos fluentes e build()).


R: É um problema pois sobrecarrega a questão dos contrutores, pode ocorrer falha só de ligar o sistema
Analise de um nova funcionalidade

public Pedido build() {
    if (cliente == null || endereco == null ) {
        throw new IllegalStateException("Cliente e endereço são obrigatórios");
    }
    return new Pedido(cliente, endereco, desconto);
}

Exercício 4: Exemplo real
Acesse o seguinte arquivo em um projeto open source:

Projeto: OkHttp (biblioteca HTTP para Java/Kotlin)
Arquivo: Request.kt
O OkHttp é uma das bibliotecas mais utilizadas para requisições HTTP em Java/Kotlin. A classe Request representa uma requisição HTTP imutável a partir do momento em que é criada.

Responda: 

Procure explicar, em linhas gerais, por que a classe Request é imutável e qual o papel do seu Builder nessa garantia.
Observe os métodos de configuração da classe Builder (por exemplo, url(...), header(...), method(...), get(), post(body)). O que esses métodos têm em comum no tipo de retorno e por que isso permite o encadeamento fluente visto em aula?
Analise o método build() e as verificações feitas nesse exemplo (por exemplo, checkNotNull(builder.url)). Relacione esse comportamento com a validação de campos obrigatórios do build() visto em aula.
O Builder desse exemplo é uma classe interna. Compare com o que foi apresentado na aula sobre Builder interno static e comente se a relação entre o Builder e o produto final é a mesma que você aprendeu.

Exercício 5: Implementação
Imagine que você foi contratado para criar o sistema de montagem de lanches de uma lanchonete.

O sistema precisa montar lanches passo a passo, permitindo:

escolher o pão (obrigatório),
escolher a proteína (obrigatória),
e adicionar itens opcionais: queijo, vegetais, molhos, ponto da carne (bemPassado) e observações.
Como as combinações são muitas, usar um construtor gigante ou vários construtores sobrecarregados deixaria o código ilegível.

Sua missão
Implemente, em Java, um sistema que monte lanches de forma fluente e passo a passo, usando o padrão Builder.

Crie a classe Lanche que seja o Produto:
Campos obrigatórios: pao e proteina.
Campos opcionais: queijo, vegetais, molho, bemPassado e observacoes.
Construtor privado — apenas o Builder instancia o objeto.
Métodos get para leitura dos campos (sem set, o objeto é imutável).
Crie a classe interna estática Lanche.Builder:
Métodos de configuração fluentes (por exemplo, setPao(...), setProteina(...), setQueijo(...), addVegetal(...), setMolho(...), setBemPassado(...)) que retornam o próprio Builder.
Um método build() que valida os campos obrigatórios (pao e proteina) e lança IllegalStateException caso algum esteja ausente.
Crie uma classe LancheDirector com pelo menos três variações de lanche (por exemplo, criarMisto(), criarXSalada() e criarEspecialDaCasa()) reutilizando o Builder.
Crie uma classe de teste, por exemplo Main, que:
Monte lanches pela API fluente, alguns apenas com os campos obrigatórios e outros com várias opções.
Gere lanches através do Director e imprima a descrição de cada um.
Tente montar um lanche sem informar um campo obrigatório e mostre que o build() lança a exceção de validação.

R:
