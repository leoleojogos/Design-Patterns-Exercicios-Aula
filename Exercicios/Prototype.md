Exercício 1: AplicaçõesRelatórios financeiros a partir de template base:Faz sentido usar Prototype. O template já possui a formatação, cabeçalho e estrutura prontos, servindo como modelo base. Clonar o protótipo evita reconstruir a base a cada mês, bastando preencher os dados variáveis.  Classe simples Ponto(x, y) em CAD:Não faz sentido usar Prototype. A classe possui poucos campos primitivos e sua criação direta por construtor é trivial e rápida. Aplicar Prototype nesse cenário traria complexidade desnecessária (overengineering).  Inimigos de jogos com muita configuração e variações:Faz sentido usar Prototype. Objetos complexos com muitas configurações prontas exigem muito código repetido se instanciados do zero. Clonar uma base permite criar variações para cada fase de forma rápida e consistente.  Módulo desacoplado de classes concretas via registro:Faz sentido usar Prototype. O uso de um registro de protótipos desacopla o cliente das classes concretas, permitindo solicitar novas cópias apenas por um identificador textual.  Classe Produto criada sem variações em ponto único:Não faz sentido usar Prototype. Como o objeto é instanciado em um único local, não possui configurações custosas e não gera variações sobre uma base comum, o construtor padrão resolve perfeitamente.  Exercício 2: AnalogiaA Fábrica de Carimbos e Formulários Padrão:Objeto modelo pronto: Uma folha de formulário timbrado padrão já diagramada, impressa e com cabeçalhos oficiais definidos.Cópias completas e prontas: Quando um novo funcionário ou cliente precisa do documento, uma cópia autenticada já nasce com todas as seções e formatações estruturadas, sem necessidade de redesenhar o documento do zero.Variações sobre a mesma base: Partindo da cópia desse formulário base, cada setor apenas preenche os campos específicos de texto ou aplica carimbos pontuais, mantendo toda a estrutura original inalterada e gerando variantes rápidas do mesmo documento.Exercício 3: Anti-patternProblema de design da cópia campo a campo:Essa abordagem fere o encapsulamento, expõe o estado interno do objeto e transfere a responsabilidade de criação para a classe consumidora (Fase). Além disso, acopla o código a getters/setters da classe concreta.  Bugs ao adicionar novos campos:Se um novo atributo for adicionado a Inimigo e o método criarCopia for esquecido, a nova propriedade será inicializada com o valor padrão da linguagem (null ou 0), gerando clones incompletos e bugs difíceis de rastrear em tempo de execução.  Compartilhamento da Arma:O código faz uma cópia rasa (shallow copy), copiando apenas a referência de memória da Arma. Caso um dos clones sofra uma modificação em sua arma, o original e todos os demais clones sofrerão a alteração simultaneamente por compartilharem a mesma instância.  Exercício 4: Exemplo realComparação entre Object.clone() e o padrão Prototype (GoF):A abordagem do GoF com construtor de cópia e interface customizada é superior e mais orientada a objetos. Ela preserva a tipagem de retorno sem necessidade de cast, dispensa tratamento de CloneNotSupportedException e respeita a execução normal dos construtores. O clone() nativo do Java contorna construtores via cópia binária de memória e acopla a classe a uma interface marcadora imperfeita.  Comportamento padrão do Object.clone():O comportamento padrão é estritamente a cópia rasa (shallow copy), duplicando atributos primitivos e apenas copiando referências de objetos agregados.  Exercício 5: ImplementaçãoArma.javaJavapublic class Arma {
    private String nome;
    private Double bonusDano;

    public Arma(String nome, Double bonusDano) {
        this.nome = nome;
        this.bonusDano = bonusDano;
    }

    public Arma clonar() {
        return new Arma(this.nome, this.bonusDano);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getBonusDano() {
        return bonusDano;
    }

    public void setBonusDano(Double bonusDano) {
        this.bonusDano = bonusDano;
    }

    @Override
    public String toString() {
        return nome + " (+" + bonusDano + " dano)";
    }
}
InimigoPrototype.javaJavapublic interface InimigoPrototype {
    InimigoPrototype clonar();
}
Inimigo.javaJavapublic class Inimigo implements InimigoPrototype {
    private String tipo;
    private Double vida;
    private Double dano;
    private Arma arma;

    public Inimigo(String tipo, Double vida, Double dano, Arma arma) {
        this.tipo = tipo;
        this.vida = vida;
        this.dano = dano;
        this.arma = arma;
    }

    // Construtor de cópia com cópia profunda da Arma
    public Inimigo(Inimigo base) {
        this.tipo = base.tipo;
        this.vida = base.vida;
        this.dano = base.dano;
        this.arma = (base.arma != null) ? base.arma.clonar() : null;
    }

    @Override
    public InimigoPrototype clonar() {
        return new Inimigo(this);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getVida() {
        return vida;
    }

    public void setVida(Double vida) {
        this.vida = vida;
    }

    public Double getDano() {
        return dano;
    }

    public void setDano(Double dano) {
        this.dano = dano;
    }

    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        this.arma = arma;
    }

    @Override
    public String toString() {
        return "[" + tipo + "] Vida: " + vida + " | Dano Base: " + dano + " | Arma: " + arma;
    }
}
RegistroDePrototipos.javaJavaimport java.util.HashMap;
import java.util.Map;

public class RegistroDePrototipos {
    private final Map<String, InimigoPrototype> prototipos = new HashMap<>();

    public RegistroDePrototipos() {
        carregarPrototipos();
    }

    private void carregarPrototipos() {
        prototipos.put("guerreiro", new Inimigo("Guerreiro", 100.0, 15.0, new Arma("Espada de Ferro", 5.0)));
        prototipos.put("mago", new Inimigo("Mago", 60.0, 25.0, new Arma("Cajado Arcano", 12.0)));
        prototipos.put("arqueiro", new Inimigo("Arqueiro", 75.0, 18.0, new Arma("Arco Curto", 7.0)));
        prototipos.put("chefe", new Inimigo("Chefe Dragão", 500.0, 50.0, new Arma("Lança Demoníaca", 30.0)));
    }

    public InimigoPrototype getPrototipo(String nome) {
        InimigoPrototype prototipo = prototipos.get(nome.toLowerCase());
        if (prototipo == null) {
            throw new IllegalArgumentException("Protótipo não encontrado: " + nome);
        }
        return prototipo.clonar();
    }
}
Main.javaJavapublic class Main {
    public static void main(String[] args) {
        RegistroDePrototipos registro = new RegistroDePrototipos();

        // 1. Obter inimigos pelo registro
        Inimigo guerreiroA = (Inimigo) registro.getPrototipo("guerreiro");
        Inimigo guerreiroB = (Inimigo) registro.getPrototipo("guerreiro");

        // 2. Criar inimigo elite e demonstrar que o protótipo não muda
        Inimigo guerreiroElite = (Inimigo) registro.getPrototipo("guerreiro");
        guerreiroElite.setTipo("Guerreiro Elite");
        guerreiroElite.setVida(220.0);
        guerreiroElite.setDano(35.0);

        System.out.println("--- Variações a partir do Protótipo ---");
        System.out.println("Clone Padrão: " + guerreiroA);
        System.out.println("Clone Elite:  " + guerreiroElite);

        // 3. Provar que clones são instâncias distintas
        System.out.println("\n--- Prova de Objetos Distintos ---");
        System.out.println("guerreiroA == guerreiroB? " + (guerreiroA == guerreiroB));
        System.out.println("Hash Guerreiro A: " + System.identityHashCode(guerreiroA));
        System.out.println("Hash Guerreiro B: " + System.identityHashCode(guerreiroB));

        // 4. Demonstrar cópia profunda da Arma
        System.out.println("\n--- Demonstração da Cópia Profunda ---");
        System.out.println("Arma Guerreiro A (antes): " + guerreiroA.getArma());
        System.out.println("Arma Guerreiro B (antes): " + guerreiroB.getArma());

        guerreiroA.getArma().setNome("Espada Flamejante");
        guerreiroA.getArma().setBonusDano(20.0);

        System.out.println("\nApós alterar apenas a arma do Guerreiro A:");
        System.out.println("Arma Guerreiro A: " + guerreiroA.getArma());
        System.out.println("Arma Guerreiro B (intacta): " + guerreiroB.getArma());
        System.out.println("Armas compartilham a mesma referência? " + (guerreiroA.getArma() == guerreiroB.getArma()));
    }
}