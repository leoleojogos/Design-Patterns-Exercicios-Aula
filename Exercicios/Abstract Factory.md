# Resolução de Exercícios: Abstract Factory

---

## Exercício 1: Aplicações

* **Uma aplicação de interface gráfica que precisa trocar o tema/plataforma (Windows, macOS, Linux):**
  * **Veredito:** Faz sentido usar Abstract Factory[cite: 2].
  * **Justificativa:** O sistema precisa criar famílias de componentes visuais interdependentes (`Botao`, `Checkbox`, `Janela`) mantendo a coerência com a plataforma ativa[cite: 2]. O padrão impede que elementos de sistemas operacionais distintos sejam misturados e isola o cliente do acoplamento direto com as implementações concretas[cite: 2].

* **Uma classe simples `Ponto(x, y)` em um sistema de CAD:**
  * **Veredito:** Não faz sentido usar Abstract Factory[cite: 2].
  * **Justificativa:** Trata-se de um objeto de valor simples, sem famílias de variantes ou dependência de compatibilidade com outros objetos. Introduzir interfaces e fábricas criaria complexidade desnecessária (*overengineering*) e perda de performance em instanciações frequentes[cite: 2].

* **Um módulo de banco de dados por fornecedor (MySQL vs. PostgreSQL):**
  * **Veredito:** Faz sentido usar Abstract Factory[cite: 2].
  * **Justificativa:** Cada fornecedor requer um conjunto específico de classes compatíveis (`Conexao`, `Comando`, `Transacao`)[cite: 2]. A fábrica abstrata assegura que uma `TransacaoMySQL` nunca seja instanciada sobre uma `ConexaoPostgreSQL`, mantendo a coerência transacional[cite: 2].

* **Uma loja que vende kits de móveis por estilo (Moderno, Vitoriano, Art Déco):**
  * **Veredito:** Faz sentido usar Abstract Factory[cite: 2].
  * **Justificativa:** Cada kit é composto por múltiplos produtos (`Cadeira`, `Sofa`, `MesaDeCentro`) que precisam combinar visualmente entre si[cite: 2]. O padrão delega a criação à fábrica concreta do estilo selecionado, assegurando consistência visual e facilitando a inclusão de novas linhas de design[cite: 2].

* **Uma classe `Produto(nome, preco, quantidadeEstoque)` sem variações:**
  * **Veredito:** Não faz sentido usar Abstract Factory[cite: 2].
  * **Justificativa:** A classe não possui famílias de variantes nem interdependências com outros produtos do sistema[cite: 2]. O uso do construtor tradicional resolve a criação diretamente, sem a necessidade de criar interfaces e fábricas adicionais[cite: 1, 2].

---

## Exercício 2: Analogia (Fora de TI)

### A Oficina de Carros de Competição

* **Famílias de itens coerentes:** Para colocar um carro na pista, é obrigatório utilizar um conjunto integrado formado por **Pneus, Suspensão e Freios**.
* **Variantes da família:**
  * **Oficina de Rali (Família Rali):** produz pneus biscoito para cascalho, suspensão elevada de curso longo e freios selados contra lama e água.
  * **Oficina de Fórmula 1 (Família F1):** produz pneus lisos (*slick*), suspensão ultra-rígida colada ao solo e discos de freio cerâmicos projetados para altíssima temperatura no asfalto.
* **Incoerência ao misturar:** Se o mecânico instalar pneus *slick* de F1 em uma suspensão alta de rali, o carro atolará na primeira poça de lama ou capotará na primeira curva rápida no asfalto. As peças falham porque não foram concebidas para o mesmo ambiente técnico.
* **Relação com o padrão:** A `AbstractFactory` atua como a contratação da oficina especializada: ao solicitar o "pacote de rali", ela fornece exclusivamente peças projetadas para operar em conjunto, impedindo trocas incorretas[cite: 2].

---

## Exercício 3: Anti-pattern

### 1. Problema de design do `new` direto
Instanciar componentes diretamente com `new` acopla a classe `Aplicacao` às classes concretas de cada sistema operacional[cite: 1, 2]. Isso viola o princípio de responsabilidade única (SRP) e o princípio Aberto/Fechado (OCP), pois qualquer suporte a um novo sistema obriga a modificação da classe e espalha estruturas de decisão condicional (`if-else`) pelo projeto[cite: 1, 2].

### 2. Bugs por mistura e impacto de um novo SO
* **Bug no Linux:** Dentro do bloco `linux`, a linha `checkbox = new CheckboxWindows();` instancia por engano um componente da família errada. O usuário verá um botão com o tema visual do Linux ao lado de uma caixa de seleção com estilo e dependências nativas do Windows, quebrando a identidade da interface gráfica e podendo gerar falhas de renderização[cite: 2].
* **Impacto com novo SO (macOS):** Exige abrir a classe existente, adicionar uma nova ramificação `else if (sistemaOperacional.equals("mac"))`[cite: 1] e repetir a instanciação manual, elevando o risco de erros por cópia e cola[cite: 1, 2].

### 3. Proposta de solução com Abstract Factory
* **Produtos Abstratos:** Interfaces `Botao` (método `renderizar()`) e `Checkbox` (método `alternar()`).
* **Fábrica Abstrata:** Uma interface `FabricaGUI` declarando `Botao criarBotao()` e `Checkbox criarCheckbox()`[cite: 2].
* **Fábricas Concretas:** 
  * `FabricaWindows`: cria `BotaoWindows` e `CheckboxWindows`[cite: 2].
  * `FabricaLinux`: cria `BotaoLinux` e `CheckboxLinux`[cite: 2].
* **Cliente (`Aplicacao`):** Recebe `FabricaGUI` via injeção de dependência no construtor[cite: 2]. A criação passa a ser:
  ```java
  public Aplicacao(FabricaGUI fabrica) {
      this.botao = fabrica.criarBotao();
      this.checkbox = fabrica.criarCheckbox();
  }
  ```
  O cliente desconhece as classes concretas e elimina todos os `if-else`[cite: 1, 2].

---

## Exercício 4: Exemplo Real (`iluwatar/java-design-patterns`)

### 1. Família de produtos e métodos da `KingdomFactory`
A interface declara a criação da família estrutural de um reino de fantasia por meio dos métodos:
* `Castle createCastle()`
* `King createKing()`
* `Army createArmy()`

### 2. Objetos no `ElfKingdomFactory` e coerência
A fábrica concreta instancia:
* `createCastle()` $\rightarrow$ `ElfCastle`
* `createKing()` $\rightarrow$ `ElfKing`
* `createArmy()` $\rightarrow$ `ElfArmy`

Se `createCastle()` retornasse por engano um `OrcCastle`, ocorreria uma perda de coerência da família[cite: 2]: arqueiros elfos e um rei elfo habitariam uma fortaleza de pedras brutas e ossos orc, violando o domínio estético e regras de consistência da aplicação[cite: 2].

### 3. Substituição de reino e o Princípio OCP
* **Alteração no cliente (`App`):** Nenhuma alteração é necessária na lógica interna da classe `App` que consome castelos, reis e exércitos[cite: 2]. Apenas a linha de inicialização que instancia a fábrica é trocada de `new ElfKingdomFactory()` para `new OrcKingdomFactory()`[cite: 2].
* **Relação com o OCP (Open/Closed Principle):** A classe cliente permanece **fechada para modificação** (seu fluxo de trabalho permanece estável) e **aberta para extensão** (novos reinos, como `OrcKingdomFactory` ou um futuro `DwarfKingdomFactory`, podem ser adicionados sem alterar o código existente)[cite: 1, 2].

---

## Exercício 5: Implementação

### 1. Interfaces dos Produtos Abstratos

```java
public interface Cadeira {
    void assentar();
}
```

```java
public interface Sofa {
    void deitar();
}
```

```java
public interface MesaDeCentro {
    void apoiar();
}
```

---

### 2. Produtos Concretos

#### Linha Moderna
```java
public class CadeiraModerna implements Cadeira {
    @Override
    public void assentar() {
        System.out.println("Sentando em uma cadeira moderna minimalista.");
    }
}

public class SofaModerno implements Sofa {
    @Override
    public void deitar() {
        System.out.println("Deitando em um sofa moderno de tecido cinza.");
    }
}


public class MesaDeCentroModerna implements MesaDeCentro {
    @Override
    public void apoiar() {
        System.out.println("Apoiando itens em uma mesa de centro moderna com tampo de vidro.");
    }
}
```

#### Linha Vitoriana
```java
public class CadeiraVitoriana implements Cadeira {
    @Override
    public void assentar() {
        System.out.println("Sentando em uma cadeira vitoriana de mogno com veludo.");
    }
}

public class SofaVitoriano implements Sofa {
    @Override
    public void deitar() {
        System.out.println("Deitando em um sofa vitoriano capitone entalhado.");
    }
}

public class MesaDeCentroVitoriana implements MesaDeCentro {
    @Override
    public void apoiar() {
        System.out.println("Apoiando itens em uma mesa de centro vitoriana dourada com marmore.");
    }
}
```

---

### 3. Fábrica Abstrata

```java
public interface FabricaMobilia {
    Cadeira criarCadeira();
    Sofa criarSofa();
    MesaDeCentro criarMesaDeCentro();
}
```

---

### 4. Fábricas Concretas

```java
public class FabricaMobiliaModerna implements FabricaMobilia {
    @Override
    public Cadeira criarCadeira() {
        return new CadeiraModerna();
    }

    @Override
    public Sofa criarSofa() {
        return new SofaModerno();
    }

    @Override
    public MesaDeCentro criarMesaDeCentro() {
        return new MesaDeCentroModerna();
    }
}
```

```java
public class FabricaMobiliaVitoriana implements FabricaMobilia {
    @Override
    public Cadeira criarCadeira() {
        return new CadeiraVitoriana();
    }

    @Override
    public Sofa criarSofa() {
        return new SofaVitoriano();
    }

    @Override
    public MesaDeCentro criarMesaDeCentro() {
        return new MesaDeCentroVitoriana();
    }
}
```

---

### 5. Cliente (`ConfiguradorDeSala`)

```java
public class ConfiguradorDeSala {
    private final Cadeira cadeira;
    private final Sofa sofa;
    private final MesaDeCentro mesa;

    public ConfiguradorDeSala(FabricaMobilia fabrica) {
        this.cadeira = fabrica.criarCadeira();
        this.sofa = fabrica.criarSofa();
        this.mesa = fabrica.criarMesaDeCentro();
    }

    public void exibir() {
        cadeira.assentar();
        sofa.deitar();
        mesa.apoiar();
    }
}
```

---

### 6. Execução e Demonstração (`Main`)

```java
public class Main {
    public static void main(String[] args) {
        System.out.println("=== SALA MODERNA ===");
        FabricaMobilia fabricaModerna = new FabricaMobiliaModerna();
        ConfiguradorDeSala salaModerna = new ConfiguradorDeSala(fabricaModerna);
        salaModerna.exibir();

        System.out.println("\n=== SALA VITORIANA ===");
        FabricaMobilia fabricaVitoriana = new FabricaMobiliaVitoriana();
        ConfiguradorDeSala salaVitoriana = new ConfiguradorDeSala(fabricaVitoriana);
        salaVitoriana.exibir();
    }
}
```

---

### README: Por que o Abstract Factory evita a mistura de famílias?

Quando um sistema instancia produtos por meio de `new` espalhados, o compilador permite combinações incoerentes, como `new CadeiraModerna()`, `new SofaVitoriano()` e `new MesaDeCentroArtDeco()`[cite: 2]. O sistema compila normalmente, mas a interface ou regra de negócio é corrompida com um kit visualmente destoante[cite: 2].

O padrão **Abstract Factory** resolve esse problema ao delegar toda a instanciação a uma fábrica concreta (`FabricaMobiliaModerna` ou `FabricaMobiliaVitoriana`)[cite: 2]. Como o cliente (`ConfiguradorDeSala`) consome apenas a interface genérica `FabricaMobilia`, é impossível instanciar uma cadeira moderna a partir da fábrica vitoriana[cite: 2]. A compatibilidade da família passa a ser garantida pela arquitetura da aplicação[cite: 2].