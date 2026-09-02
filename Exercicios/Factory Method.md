# Resolução dos Exercícios: Abstract Factory

---

## Exercício 1: Aplicações

* **Interface gráfica multiplataforma (Windows, macOS, Linux)**
  * **Veredito:** Faz sentido usar Abstract Factory.
  * **Justificativa:** O cenário exige famílias de componentes visuais compatíveis entre si (botão, checkbox, janela). O padrão assegura que elementos de plataformas distintas não sejam misturados na mesma tela e desacopla o código cliente das implementações concretas de cada SO.

* **Classe simples `Ponto(x, y)` em sistema CAD**
  * **Veredito:** Não faz sentido usar Abstract Factory.
  * **Justificativa:** É um objeto de valor simples, sem variações de família ou regras de compatibilidade cruzada. Criar interfaces e fábricas para coordenadas geraria complexidade desnecessária (*overengineering*)[cite: 1] e perda de performance em instanciações frequentes.

* **Módulo de banco de dados por fornecedor (MySQL vs. PostgreSQL)**
  * **Veredito:** Faz sentido usar Abstract Factory.
  * **Justificativa:** Cada fornecedor requer uma família de classes que devem operar juntas (`Conexao`, `Comando`, `Transacao`). A fábrica abstrata impede que uma `TransacaoMySQL` seja executada sobre uma `ConexaoPostgreSQL`, mantendo a coerência do sistema.

* **Loja de kits de móveis por estilo (Moderno, Vitoriano, Art Déco)**
  * **Veredito:** Faz sentido usar Abstract Factory.
  * **Justificativa:** O kit é composto por múltiplos produtos (`Cadeira`, `Sofa`, `MesaDeCentro`) que precisam combinar visualmente. O padrão centraliza a montagem na fábrica do estilo selecionado, impedindo composições misturadas e facilitando novos lançamentos.

* **Classe `Produto(nome, preco, quantidadeEstoque)` sem variações**
  * **Veredito:** Não faz sentido usar Abstract Factory.
  * **Justificativa:** Não existem variantes, temas ou dependências de família para justificar abstrações adicionais. O construtor comum atende ao requisito com simplicidade.

---

## Exercício 2: Analogia (Fora de TI)

### A Oficina de Carros de Competição

* **Família de itens:** Para competir em alto nível, um carro precisa de três peças ajustadas entre si: **Pneus, Suspensão e Freios**.
* **Variantes da família:**
  * **Oficina de Rali:** produz pneus biscoito para cascalho, suspensão elevada com curso longo e freios vedados contra lama.
  * **Oficina de Fórmula 1:** produz pneus lisos (*slick*), suspensão rígida rebaixada ao solo e discos de freio cerâmicos de alto calor.
* **Incoerência da mistura:** Se o mecânico instalar a suspensão alta de rali com pneus lisos de F1, o carro atolará na primeira poça de terra ou perderá aderência na pista de asfalto. Os componentes só funcionam adequadamente quando pertencem ao mesmo conjunto técnico.
* **Relação com o padrão:** A fábrica abstrata funciona como a escolha da oficina parceira: ao encomendar o "pacote de rali", ela entrega todos os itens correspondentes e prontos para trabalhar em harmonia.

---

## Exercício 3: Anti-pattern

### 1. Problema de design do uso de `new`
Instanciar classes diretamente com `new` gera forte acoplamento entre a classe cliente (`Aplicacao`) e as implementações concretas[cite: 1]. Qualquer suporte a um novo sistema exige alterar uma classe funcional e espalha comandos condicionais (`if-else`) pelo projeto[cite: 1].

### 2. Bugs por mistura e expansão para novo SO
* **Bug no código:** No bloco `linux`, a linha `checkbox = new CheckboxWindows();` mistura temas. O usuário verá um botão com estilo Linux ao lado de uma caixa de seleção com visual e dependências nativas do Windows, quebrando a estética e podendo gerar erros em tempo de execução.
* **Impacto com novo SO (macOS):** Exige abrir o arquivo, adicionar outro `else if`[cite: 1] e repetir a lógica manual de instanciação, aumentando o risco de novos erros de cópia e cola.

### 3. Solução com Abstract Factory
* **Produtos Abstratos:** Interfaces `Botao` (método `renderizar()`) e `Checkbox` (método `alternar()`).
* **Fábrica Abstrata:** Interface `GUIFactory` com os métodos `Botao criarBotao()` e `Checkbox criarCheckbox()`.
* **Fábricas Concretas:**
  * `WindowsFactory`: instancia `BotaoWindows` e `CheckboxWindows`.
  * `LinuxFactory`: instancia `BotaoLinux` e `CheckboxLinux`.
* **Cliente (`Aplicacao`):** Passa a receber a interface `GUIFactory` via injeção de dependência no construtor. Ele chama apenas `factory.criarBotao()` e `factory.criarCheckbox()`, sem conhecer as classes concretas nem usar condicionais[cite: 1].

---

## Exercício 4: Exemplo Real (`iluwatar/java-design-patterns`)

### 1. Família de produtos e métodos da `KingdomFactory`
A interface declara a família de componentes de um reino fantástico:
* `Castle createCastle()`
* `King createKing()`
* `Army createArmy()`

### 2. Objetos no `ElfKingdomFactory` e coerência
* **Objetos instanciados:**
  * `createCastle()` $\rightarrow$ `ElfCastle`
  * `createKing()` $\rightarrow$ `ElfKing`
  * `createArmy()` $\rightarrow$ `ElfArmy`
* **Quebra de coerência:** Se `createCastle()` retornasse acidentalmente um `OrcCastle`, o jogo teria arqueiros e um rei elfo vivendo em uma fortaleza de ossos e pedras brutas orc, ferindo a lógica do universo ficcional e regras internas da aplicação.

### 3. Substituição de reino e o Princípio OCP
* **Alteração no cliente (`App`):** O código interno da classe `App` que consome castelos, reis e exércitos **não muda**. Apenas a fábrica passada como argumento na inicialização é trocada de `ElfKingdomFactory` para `OrcKingdomFactory`.
* **Relação com o OCP (Open/Closed Principle):** A aplicação fica **fechada para alterações** (sua lógica permanece estável e intocada) e **aberta para extensões** (novos reinos podem ser criados apenas implementando uma nova classe de fábrica, sem risco de quebrar o código existente)[cite: 1].