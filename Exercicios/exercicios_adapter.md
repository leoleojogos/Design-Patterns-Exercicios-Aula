# Resolução dos Exercícios — Design Pattern: Adapter

---

## Exercício 1: Aplicações

Para cada cenário abaixo, avalia-se a aplicabilidade do padrão **Adapter**:

### 1. Módulo de previsão do tempo com dois SDKs externos de APIs diferentes
* **Decisão:** **Faz sentido usar Adapter.**
* **Justificativa:** Os SDKs externos são bibliotecas de terceiros com interfaces incompatíveis e código não modificável. A criação de adaptadores isola as particularidades de cada fornecedor e permite que a aplicação consuma os dados por meio de uma única interface comum e padronizada.

### 2. Substituição de biblioteca legada de e-mail por uma nova sem alterar os serviços clientes
* **Decisão:** **Faz sentido usar Adapter.**
* **Justificativa:** A nova biblioteca possui assinaturas e tipos distintos do código legado já integrado. O adaptador atua como tradutor entre a interface esperada pelos serviços da aplicação e a nova biblioteca, evitando refatorações invasivas em múltiplos pontos do sistema.

### 3. Criação preventiva de adaptadores para todas as classes sem incompatibilidade real
* **Decisão:** **Não faz sentido usar Adapter.**
* **Justificativa:** Essa abordagem configura *overengineering* (complexidade desnecessária), pois introduz camadas redundantes de código sem resolver nenhum problema real. O padrão Adapter deve ser empregado para solucionar incompatibilidades concretas com código legado ou de terceiros, não de forma especulativa.

### 4. Checkout com integração de SDK de pagamento de terceiros
* **Decisão:** **Faz sentido usar Adapter.**
* **Justificativa:** O SDK de pagamentos é fechado para modificações e dita sua própria assinatura de métodos e estruturas de dados. O adaptador desacopla as regras de negócio do checkout da dependência direta da biblioteca externa, facilitando a manutenção e a adição de novos meios de pagamento no futuro.

---

## Exercício 2: Analogia

### Analogia: O Fone com Plugue P2 e o Smartphone com Porta USB-C

Imagine a situação cotidiana de tentar conectar um fone de ouvido clássico com fio a um smartphone moderno.

* **Formatos/Padrões Incompatíveis:**
  O smartphone moderno possui apenas uma porta digital retangular (USB-C), enquanto o fone de ouvido possui exclusivamente um conector analógico cilíndrico de 3,5 mm (plugue P2). É fisicamente e eletricamente impossível encaixá-los de forma direta.

* **Intermediário Funcional (O Adaptador):**
  Um *dongle* adaptador (USB-C macho para P2 fêmea) é posicionado entre os dois. Ele recebe o sinal digital do smartphone, converte-o internamente em sinal de áudio analógico e entrega na entrada P2 para o fone. Ambos funcionam juntos perfeitamente sem que ninguém precise abrir o smartphone para furar uma entrada de 3,5 mm nem cortar o cabo do fone para soldar um conector USB-C.

* **Independência dos Lados:**
  Se você trocar de fone por qualquer outro fone com conector P2, ele funcionará no mesmo adaptador. Se você conectar o mesmo fone em um computador antigo que já possui saída P2 nativa, basta remover o adaptador e usá-lo diretamente, sem que nenhum dos lados sofra alteração.

---

## Exercício 3: Anti-pattern

### 1. Por que chamar os fornecedores diretamente é um problema de design?
* **Alto Acoplamento:** A classe `PainelClima` fica amarrada às implementações concretas dos SDKs (`AccuWeatherApi` e `OpenWeatherApi`), tornando-se refém de bibliotecas externas.
* **Violação do SRP (Princípio da Responsabilidade Única):** O painel assume múltiplas funções: orquestrar regras de negócio, instanciar APIs, converter unidades e exibir dados em tela.
* **Duplicação de Código:** Como outros dois módulos da aplicação fazem a mesma consulta, a lógica de checagem condicional e as fórmulas de conversão ($°F \rightarrow °C$) acabam copiadas e coladas por todo o projeto.

### 2. Dificuldades e bugs decorrentes de mudanças
* **Violação do OCP (Princípio Aberto/Fechado):** Para adicionar um novo fornecedor ou ajustar um SDK que mudou sua assinatura/unidade, é necessário alterar manualmente todos os blocos `if/else` espalhados pelo sistema.
* **Inconsistências e Bugs Silenciosos:** Se a fórmula de conversão for corrigida ou alterada em um módulo e esquecida em outro, a aplicação passará a exibir valores de temperatura conflitantes para a mesma cidade.
* **Dificuldade de Testes:** Torna-se inviável realizar testes unitários isolados em `PainelClima`, pois a instanciação direta (`new AccuWeatherApi()`) impede a injeção de mocks ou stubs.

### 3. Proposta de Solução com o Padrão Adapter
* **Target (Interface do Cliente):** Define-se uma interface comum, por exemplo `PrevisaoService`, contendo o método `int obterTemperatura(String cidade)`, padronizando a unidade sempre em graus Celsius.
* **Adapters por Fornecedor:** Cria-se um adaptador para cada fornecedor (`AccuWeatherAdapter` e `OpenWeatherAdapter`), ambos implementando `PrevisaoService`. Cada classe recebe sua respectiva API externa por composição e encapsula as chamadas específicas e fórmulas de conversão.
* **Cliente Desacoplado:** `PainelClima` deixa de conter lógica condicional e instanciações de APIs. O painel passa a receber apenas uma referência de `PrevisaoService` em seu construtor, interagindo exclusivamente com o contrato da interface.

---

## Exercício 4: Exemplo Real (`InputStreamReader`)

### 1. Interfaces Incompatíveis e Estrutura da Classe
* **Interfaces que liga:** O `InputStreamReader` faz a ponte entre a API orientada a bytes binários (`InputStream`) e a API orientada a caracteres de texto (`Reader`).
* **Herança e Construtor:** A classe estende `java.io.Reader` (tipo que o cliente utiliza) e recebe no construtor uma instância de `java.io.InputStream`, opcionalmente acompanhada de uma especificação de codificação de caracteres (`Charset` ou `CharsetDecoder`).

### 2. Delegação e Visão do Código Cliente
* **Delegação:** Métodos como `read()`, `ready()` e `close()` não manipulam bytes diretamente; eles delegam a execução para o objeto interno `StreamDecoder sd`, que lê os bytes brutos do `InputStream` e os converte em caracteres.
* **Por que o cliente não conhece o InputStream:** O cliente precisa apenas ler caracteres e textos (`char[]`, `String`), operando com o contrato de alto nível de `Reader`. A complexidade de ler blocos de bytes, gerenciar offsets e aplicar tabelas de decodificação de caracteres fica totalmente encapsulada.

### 3. Conversão e Variação do Padrão
* **Local da conversão:** A conversão de bytes para caracteres ocorre no decodificador interno referenciado pelo campo `private final StreamDecoder sd`.
* **Variação do Adapter:** Representa o **Adaptador de Objeto** (*Object Adapter*), pois utiliza composição — guarda uma referência para a instância de `InputStream` em vez de utilizar herança múltipla.
* **Compatibilidade universal:** Funciona com qualquer implementação de `InputStream` (`FileInputStream`, `SocketInputStream`, `ByteArrayInputStream`, etc.) graças ao polimorfismo garantido pela classe abstrata base.

---

## Exercício 5: Implementação

### Código-Fonte em Java

```java


public interface PrevisaoService {
    int obterTemperatura(String cidade);
}


class OpenWeatherApi {
    public double temperatura(String cidade)
        return 23.8;
    }
}

class AccuWeatherApi {
    public int getTemperature(String cidade) {
        return 77;
    }
}


class OpenWeatherAdapter implements PrevisaoService {
    private final OpenWeatherApi api;

    public OpenWeatherAdapter(OpenWeatherApi api) {
        this.api = api;
    }

    @Override
    public int obterTemperatura(String cidade) {
        double tempCelsius = api.temperatura(cidade);
        return (int) tempCelsius;
    }
}

class AccuWeatherAdapter implements PrevisaoService {
    private final AccuWeatherApi api;

    public AccuWeatherAdapter(AccuWeatherApi api) {
        this.api = api;
    }

    @Override
    public int obterTemperatura(String cidade) {
        int fahrenheit = api.getTemperature(cidade);
        return (fahrenheit - 32) * 5 / 9;
    }
}


class PainelClima {
    private final PrevisaoService previsaoService;

    public PainelClima(PrevisaoService previsaoService) {
        this.previsaoService = previsaoService;
    }

    public void exibir(String cidade) {
        int temp = previsaoService.obterTemperatura(cidade);
        System.out.println("Temperatura em " + cidade + ": " + temp + "°C");
    }
}


public class Main {
    public static void main(String[] args) {
        String cidade = "Criciúma";

        
        PrevisaoService openWeather = new OpenWeatherAdapter(new OpenWeatherApi());
        PainelClima painelOpen = new PainelClima(openWeather);
        painelOpen.exibir(cidade);

       
        PrevisaoService accuWeather = new AccuWeatherAdapter(new AccuWeatherApi());
        PainelClima painelAccu = new PainelClima(accuWeather);
        painelAccu.exibir(cidade);
    }
}
```

### Extensibilidade (Inclusão de um Terceiro Fornecedor)

Para adicionar um novo fornecedor (como uma `ClimaTempoApi` com método `consultar(String cid)` que devolve uma `String` ou temperatura em Kelvin):
1. **Não se altera `PainelClima` nem `PrevisaoService`:** O código do cliente permanece intacto.
2. **Cria-se apenas uma nova classe adaptadora:** Por exemplo, `ClimaTempoAdapter implements PrevisaoService`, que encapsula a `ClimaTempoApi` e converte a resposta para `int` em graus Celsius.
3. **Injeta-se a nova implementação:** Basta passar `new ClimaTempoAdapter(new ClimaTempoApi())` para o `PainelClima`.

Isso atende plenamente ao **Princípio Aberto/Fechado (OCP)**: o sistema está aberto para extensão através de novos adaptadores, mas fechado para modificação nas classes clientes existentes.
