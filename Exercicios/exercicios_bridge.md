# Resolução dos Exercícios — Design Pattern: Bridge

---

## Exercício 1: Aplicações

Para cada cenário abaixo, avalia-se a aplicabilidade do padrão **Bridge**:

### 1. Sistema de notificações com variações de conteúdo e canais de envio
* **Decisão:** **Faz sentido usar Bridge.**
* **Justificativa:** O problema apresenta duas dimensões ortogonais e independentes de variação: o tipo de mensagem (conteúdo/regra de negócio) e o meio de transporte (canal de envio). Sem o padrão, ocorreria uma explosão de subclasses ($N \times M$), enquanto o Bridge desacopla a abstração da notificação do mecanismo concreto de entrega.

### 2. Aplicação multiplataforma executando lógica sobre APIs de SO diferentes (Windows, Linux, macOS)
* **Decisão:** **Faz sentido usar Bridge.**
* **Justificativa:** Este é o cenário clássico do padrão, separando a camada de alto nível da interface gráfica ou lógica do aplicativo das implementações de baixo nível específicas de cada sistema operacional. A aplicação evolui suas funcionalidades sem multiplicar classes para cada plataforma suportada.

### 3. Módulo de persistência com repositório para MySQL e PostgreSQL com troca em runtime
* **Decisão:** **Faz sentido usar Bridge.**
* **Justificativa:** A abstração (operações de negócio do repositório) é separada da implementação (drivers e dialetos de banco de dados específicos). O padrão evita a criação de subclasses combinatórias e permite alternar a implementação concreta dinamicamente durante a execução da aplicação.

### 4. Framework de testes executando em diferentes ambientes e configurações de hardware
* **Decisão:** **Faz sentido usar Bridge.**
* **Justificativa:** Há uma clara divisão entre a abstração dos casos/ciclos de vida dos testes e as implementações primitivas de controle dos ambientes e hardwares. O desacoplamento via composição permite adicionar novos tipos de testes ou novos hardwares sem gerar dependência direta entre as duas hierarquias.

---

## Exercício 2: Analogia

### Analogia: Luminárias Decorativas e Fontes de Luz (Lâmpadas)

Pense na fabricação e no uso de sistemas de iluminação residencial.

* **Duas Famílias Independentes:**
  * **Família 1 (Suporte/Estilo):** Os modelos de luminária (Luminária de mesa articulada, Pendente de teto, Arandela de parede).
  * **Família 2 (Mecanismo de Iluminação):** As tecnologias de lâmpada (Lâmpada LED branca, Lâmpada incandescente vintage dimerizável, Lâmpada inteligente RGB conectada via Wi-Fi).

* **Inviabilidade da Duplicação (Herança Rígida):**
  Se as luminárias fossem fabricadas com as lâmpadas fundidas/soldadas em sua estrutura, os fabricantes teriam que produzir dezenas de variantes monolíticas: *PendenteComLed*, *PendenteComVintage*, *ArandelaComLed*, *ArandelaComRGB*, etc. Qualquer novo modelo de cúpula ou nova tecnologia de luz obrigaria a criação de múltiplos produtos duplicados.

* **Composição e Evolução Independente (A "Ponte"):**
  O soquete padrão (bocal E27) funciona como a **Bridge**. A luminária atua como a abstração (responsável pela estrutura, posicionamento e estética) e apenas delega a emissão de luz à lâmpada instalada por composição. O designer pode lançar novas luminárias sem saber nada de semicondutores de LED, e os fabricantes de lâmpadas podem inventar lâmpadas mais eficientes sem redesenhar o catálogo de luminárias do mundo. A combinação é feita na hora da montagem.

---

## Exercício 3: Anti-pattern

### 1. Por que essa hierarquia (Forma × Cor) é um problema de design?
* **Explosão Combinatória de Subclasses:** O uso de herança para combinar duas dimensões independentes faz o número de classes crescer multiplicativamente ($N \times M$).
* **Violação do SRP (Princípio da Responsabilidade Única):** As subclasses combinadas acumulam duas responsabilidades distintas em uma única unidade: a geometria da forma e os detalhes de estilo/renderização da cor.
* **Duplicação de Código:** A lógica de cálculo e desenho do círculo, por exemplo, é duplicada em `CirculoVermelho` e `CirculoAzul`, dificultando a manutenção.

### 2. O que acontece ao adicionar novas formas e cores?
* **Ao adicionar 1 forma (ex.: Triangulo):** É obrigatório criar $M$ novas subclasses para cobrir as cores existentes (`TrianguloVermelho`, `TrianguloAzul`).
* **Ao adicionar 1 cor (ex.: Verde):** É obrigatório criar $N$ novas subclasses para cobrir todas as formas existentes (`CirculoVerde`, `QuadradoVerde`, etc.).
* **Fórmula Geral:** Para $N$ formas e $M$ cores, existem $N \times M$ subclasses. Com 10 formas e 5 cores, seriam necessárias **50 classes** apenas para cobrir as combinações.

### 3. Proposta de Solução com Bridge
A solução consiste em trocar herança por composição, separando o problema em duas hierarquias independentes:

1. **Hierarquia de Abstração (`Forma`):**
   * Classe abstrata `Forma`, contendo uma referência protegida à interface `Cor` (`protected Cor cor;`) recebida no construtor.
   * Subclasses refinadas: `Circulo`, `Quadrado`, `Triangulo`. Elas definem apenas as regras geométricas e delegam a aplicação cromática para o objeto `Cor`.
2. **Hierarquia de Implementação (`Cor`):**
   * Interface `Cor` com operações primitivas (ex.: `String preencher()`).
   * Implementações concretas: `Vermelho`, `Azul`, `Verde`.
3. **Vantagem de Manutenção:**
   * O crescimento passa a ser aditivo: $N + M$. Com 10 formas e 5 cores, são necessárias apenas **15 classes** ($10 + 5$) em vez de 50.

---

## Exercício 4: Exemplo Real (`OpenJFX`: Skin.java e Button.java)

### 1. As duas hierarquias do padrão
* **Hierarquia de Abstração (Controle / Lógica):** A classe base `javafx.scene.control.Control` e suas especializações de interface de usuário (como `Button`, `CheckBox`, `TextField`). Essa hierarquia representa a camada de alto nível, responsável pelo modelo de dados, propriedades, estados (ex.: focado, desabilitado) e captura de eventos lógicos.
* **Hierarquia de Implementação (Skin / Renderização):** A interface `javafx.scene.control.Skin` e suas implementações concretas (como `ButtonSkin`, `CheckBoxSkin`). Essa hierarquia representa a parte de baixo nível, responsável pela árvore de nós visuais, estilização CSS e desenho gráfico dos componentes na tela.

### 2. O método `createDefaultSkin()` em `Button.java`
* **Retorno:** O método instancia e retorna uma nova implementação concreta padrão: `new ButtonSkin(this)`.
* **Motivo da Delegação:** O `Button` delega sua aparência ao `ButtonSkin` para manter estrita separação entre a lógica do controle e a sua representação visual. Isso permite trocar completamente o tema, os desenhos ou os efeitos visuais do botão em tempo de execução sem mexer na lógica de manipulação de cliques, acessibilidade ou estados do botão.

---

## Exercício 5: Implementação

### Código-Fonte em Java

```java

public interface Motor {
    void ligar();
    void acelerar();
}


class MotorGasolina implements Motor {
    @Override
    public void ligar() {
        System.out.println("Motor a gasolina dando partida... Vrummm!");
    }

    @Override
    public void acelerar() {
        System.out.println("Motor a gasolina injetando combustível e roncando forte.");
    }
}

class MotorEletrico implements Motor {
    @Override
    public void ligar() {
        System.out.println("Motor elétrico inicializado em silêncio absoluto.");
    }

    @Override
    public void acelerar() {
        System.out.println("Motor elétrico entregando torque instantâneo sem ruído.");
    }
}


abstract class Veiculo {
    protected Motor motor; 

    public Veiculo(Motor motor) {
        this.motor = motor;
    }

    public void dirigir() {
        motor.ligar();
    }

    public void acelerar() {
        motor.acelerar();
    }
}


class Sedan extends Veiculo {
    public Sedan(Motor motor) {
        super(motor);
    }

    @Override
    public void dirigir() {
        System.out.print("[Sedan] Condução suave e confortável: ");
        super.dirigir();
    }

    @Override
    public void acelerar() {
        System.out.print("[Sedan] Acelerando na rodovia: ");
        super.acelerar();
    }
}

class Suv extends Veiculo {
    public Suv(Motor motor) {
        super(motor);
    }

    @Override
    public void dirigir() {
        System.out.print("[SUV] Posição de dirigir elevada e robusta: ");
        super.dirigir();
    }

    @Override
    public void acelerar() {
        System.out.print("[SUV] Tracionando com estabilidade: ");
        super.acelerar();
    }
}


public class Main {
    public static void main(String[] args) {
        Motor motorGasolina = new MotorGasolina();
        Motor motorEletrico = new MotorEletrico();

        System.out.println("=== 1. Sedan com Motor a Gasolina ===");
        Veiculo sedanGasolina = new Sedan(motorGasolina);
        sedanGasolina.dirigir();
        sedanGasolina.acelerar();

        System.out.println("\n=== 2. Sedan com Motor Elétrico ===");
        Veiculo sedanEletrico = new Sedan(motorEletrico);
        sedanEletrico.dirigir();
        sedanEletrico.acelerar();

        System.out.println("\n=== 3. SUV com Motor a Gasolina ===");
        Veiculo suvGasolina = new Suv(motorGasolina);
        suvGasolina.dirigir();
        suvGasolina.acelerar();

        System.out.println("\n=== 4. SUV com Motor Elétrico ===");
        Veiculo suvEletrico = new Suv(motorEletrico);
        suvEletrico.dirigir();
        suvEletrico.acelerar();
    }
}
```

### Extensibilidade e Independência das Hierarquias

Com o padrão **Bridge**, qualquer evolução no sistema é **linear ($N + M$)** e não multiplicativa ($N \times M$):

* **Para adicionar um novo motor (ex.: `MotorHibrido`):**
  Basta criar a classe `MotorHibrido implements Motor`. Nenhum veículo existente (`Veiculo`, `Sedan`, `Suv`) precisa ser alterado.
* **Para adicionar um novo veículo (ex.: `Picape`):**
  Basta criar a classe `Picape extends Veiculo`. Nenhum motor (`MotorGasolina`, `MotorEletrico`) precisa ser modificado.

As duas dimensões variam de forma ortogonal, respeitando o **Princípio de Responsabilidade Única (SRP)** e o **Princípio Aberto/Fechado (OCP)**.
