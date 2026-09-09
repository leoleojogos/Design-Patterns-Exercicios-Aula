Exercício 1: AplicaçõesMódulo de previsão do tempo que consulta dois fornecedores externos com APIs diferentes: Faz sentido usar Adapter. Os SDKs externos são fechados para modificação e possuem assinaturas e formatos incompatíveis entre si. O adaptador padroniza a comunicação sob um contrato único, permitindo que a aplicação consulte ambos de maneira uniforme.  Aplicação que pretende trocar uma biblioteca legada de e-mail por outra sem alterar os serviços clientes: Faz sentido usar Adapter. A nova biblioteca provavelmente possui interfaces e tipos diferentes da biblioteca antiga. Um adaptador faz a ponte entre as chamadas existentes dos serviços e a nova biblioteca, evitando refatorações em cascata pelo sistema.  Time criando adaptadores preventivos para todas as classes do sistema sem incompatibilidade real: Não faz sentido usar Adapter. Essa abordagem incorre em overengineering, introduzindo código intermediário redundante e manutenção desnecessária sem resolver nenhum problema prático de incompatibilidade. O padrão deve ser aplicado quando há incompatibilidade concreta com código legado ou de terceiros.  Checkout que precisa integrar um SDK de pagamento de terceiros: Faz sentido usar Adapter. SDKs de pagamento de terceiros possuem modelos de dados e métodos específicos que não podem ser alterados. O adaptador isola as regras do checkout da dependência direta da biblioteca externa, facilitando substituições futuras ou a inclusão de novos gateways.  Exercício 2: AnalogiaImagine um fone de ouvido convencional de alta fidelidade com fio que termina em um plugue analógico P2 (3,5 mm) e um smartphone moderno que possui unicamente uma porta USB-C para conexão de dados e energia.Formatos incompatíveis: O smartphone só disponibiliza uma porta fêmea retangular digital (USB-C), enquanto o fone de ouvido possui apenas um plugue macho cilíndrico analógico (conector P2 de 3,5 mm). É fisicamente e eletricamente impossível conectá-los diretamente.Intermediário funcional: O dongle adaptador (USB-C para P2 fêmea) atua no meio do caminho. Ele recebe os sinais digitais da saída USB-C do telefone, converte-os internamente em sinais de áudio analógicos e os entrega na entrada correta para o fone, sem que você precise desmontar o smartphone para instalar uma entrada P2 nem cortar o cabo do fone para soldar um conector USB-C.Independência dos lados: Se você trocar de fone de ouvido por outro modelo qualquer com cabo P2, ele continuará funcionando no mesmo adaptador. Da mesma forma, se você levar seu fone para um computador antigo que já tenha saída P2 nativa, basta desconectar o adaptador e o fone continua funcionando sem nenhuma alteração.Exercício 3: Anti-patternProblema de design: O PainelClima fica fortemente acoplado às implementações concretas dos SDKs externos e acumula múltiplas responsabilidades (decidir o fornecedor, instanciar APIs, converter unidades e exibir dados), violando o Princípio da Responsabilidade Única (SRP). Além disso, a lógica de conversão e a estrutura de decisão repetem-se nos outros módulos da aplicação, gerando duplicação de código.  Dificuldades de manutenção e evolução: A inclusão de um novo fornecedor ou qualquer alteração na assinatura/comportamento de um SDK (como mudar o retorno para Fahrenheit) exige localizar e alterar manualmente todos os blocos if/else espalhados pelo sistema. Isso viola o Princípio Aberto/Fechado (OCP) e gera alto risco de bugs silenciosos, como temperaturas calculadas incorretamente caso algum módulo seja esquecido.  Proposta de solução com Adapter:Target (Interface do cliente): Criar a interface PrevisaoService com o método int obterTemperatura(String cidade), que define o contrato padrão sempre em graus Celsius.  Adapters: Criar AccuWeatherAdapter e OpenWeatherAdapter, ambos implementando PrevisaoService e recebendo sua respectiva API externa por composição. Cada adaptador isola a chamada ao SDK e a regra de conversão de tipos ou unidades.  Cliente desacoplado: O PainelClima deixa de ter blocos condicionais e recebe uma instância de PrevisaoService (via injeção de dependência no construtor). O painel interage unicamente com o método obterTemperatura, tornando-se completamente agnóstico em relação a fornecedores externos.  Exercício 4: Exemplo real (InputStreamReader)Interfaces incompatíveis e assinatura: O InputStreamReader faz a ponte entre o fluxo de entrada orientado a bytes brutos (InputStream) e o fluxo orientado a caracteres de texto (Reader). A classe estende java.io.Reader (atuando como o tipo que o cliente consome) e recebe uma instância de InputStream em seus construtores (junto com o nome, objeto ou decodificador do charset desejado).  Delegação e visão do cliente: Métodos como read(), ready() e close() delegam a execução diretamente para a ponte de decodificação interna (StreamDecoder sd), que gerencia a leitura dos bytes do InputStream subjacente e os transforma em caracteres decodificados. O código cliente enxerga apenas a API abstrata de Reader, pois seu único objetivo é manipular texto legível (char), ficando protegido dos detalhes de buffers binários, manipulação de sockets ou conversões de bytes.Mecanismo de conversão e variação do padrão: A conversão de bytes para caracteres ocorre no decodificador encapsulado pelo campo private final StreamDecoder sd. Essa classe representa a variação de Adaptador de Objeto (Object Adapter), pois emprega composição (armazena uma referência do InputStream em vez de utilizar herança múltipla). Ela funciona com qualquer InputStream (FileInputStream, SocketInputStream, ByteArrayInputStream, etc.) graças ao polimorfismo da classe base abstrata InputStream.  Exercício 5: ImplementaçãoJava// 1. Interface do cliente (Target)
public interface PrevisaoService {
    int obterTemperatura(String cidade);
}

// 2. SDKs de terceiros existentes (não modificáveis)
class OpenWeatherApi {
    public double temperatura(String cidade) {
        // Simulação: retorna valor em °C como double
        return 23.8;
    }
}

class AccuWeatherApi {
    public int getTemperature(String cidade) {
        // Simulação: retorna valor em °F como int
        return 77;
    }
}

// 3. Adaptador para OpenWeatherApi
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

// 3. Adaptador para AccuWeatherApi
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

// 4. Classe Cliente
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

// 5. Demonstração de uso
public class Main {
    public static void main(String[] args) {
        String cidade = "Criciúma";

        // Usando o fornecedor OpenWeather
        PrevisaoService openWeather = new OpenWeatherAdapter(new OpenWeatherApi());
        PainelClima painelOpen = new PainelClima(openWeather);
        painelOpen.exibir(cidade);

        // Usando o fornecedor AccuWeather sem alterar o PainelClima
        PrevisaoService accuWeather = new AccuWeatherAdapter(new AccuWeatherApi());
        PainelClima painelAccu = new PainelClima(accuWeather);
        painelAccu.exibir(cidade);
    }
}
Para integrar um terceiro fornecedor (como uma ClimaTempoApi com método consultar(String cid) que devolve uma string ou escala Kelvin), não é necessário tocar em nenhuma linha de código da classe PainelClima. Basta criar uma nova classe ClimaTempoAdapter implements PrevisaoService, instanciar a nova API dentro dela, realizar a devida conversão e repassar a nova instância do adaptador ao construtor de PainelClima. Isso respeita o Princípio Aberto/Fechado (OCP), mantendo a aplicação aberta para extensão e fechada para modificação. 