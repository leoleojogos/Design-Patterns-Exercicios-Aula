🧩 Design Patterns - Exercícios e Implementações

Repositório dedicado ao estudo, implementação prática e exercícios da disciplina de Design Patterns (Padrões de Projeto), explorando soluções reutilizáveis para problemas comuns de arquitetura e orientação a objetos em Java 17.

📂 Padrões Implementados

Criacionais (Creational):

Singleton, Factory Method, Abstract Factory, Builder, Prototype

Comportamentais (Behavioral):

Strategy — Encapsulamento de famílias de algoritmos intercambiáveis em tempo de execução (Atividade 12).

Iterator — Travessia sequencial, embaralhada e filtrada sem expor a estrutura interna da coleção (Atividade 13).

Mediator — Coordenação centralizada de comunicação entre objetos, eliminando acoplamento em malha (Atividade 14).

Observer — Notificação automática um-para-muitos orientada a eventos entre sujeito e observadores (Atividade 15).

Memento — Captura e restauração de snapshots imutáveis de estado preservando o encapsulamento (Atividade 16).

Command — Encapsulamento de requisições como objetos com suporte a macros e desfazer (undo) (Atividade 17).

🛠️ Tecnologias e Ferramentas

Linguagem: Java 17

Gerenciador de Dependências / Build: Maven

Documentação: Markdown & Diagramas Mermaid (Antes/Depois)

▶️ Como Executar os Projetos

Cada atividade possui seu próprio projeto Maven. Para compilar e executar qualquer um dos padrões via terminal:

mvn compile
java -cp target/classes br.venson.net.designpatterns.<padrao>.Main


(Substitua <padrao> por strategy, iterator, mediator, observer, memento ou command.)
