Cenários e Analogia (Ex 1 e 2)
Configurações de aplicação: Faz sentido. Garante unicidade e evita gargalos de leitura repetida no disco.

Conexões HTTP: Não faz sentido. Módulos precisam de instâncias e parâmetros independentes (ex: timeouts diferentes) para chamadas distintas.

Logger: Faz sentido. Centralizar o acesso a um arquivo único previne conflitos de concorrência e o bloqueio do sistema operacional.

Usuário Autenticado: Não faz sentido. O escopo do usuário pertence à sua sessão individual; um Singleton misturaria os dados de todos.

Cache: Faz sentido. Um único ponto em memória compartilhado por todos garante acesso rápido e economia de recursos no banco de dados.

Analogia: O Presidente de um país. Existe apenas um governante em exercício (instância única) cujas decisões afetam todo o território (ponto de acesso global). A limitação da analogia é que um presidente é substituído ao fim do mandato, enquanto o Singleton tradicional não "morre" até a aplicação ser desligada.

O Singleton como Anti-pattern (Ex 3)
Usar Singleton no carrinho de compras cria um estado global compartilhado entre todos os clientes. Bugs críticos ocorrerão: o Cliente A poderá ver, alterar ou até pagar pelos itens que o Cliente B acabou de adicionar, gerando falhas de segurança e condição de corrida (race conditions).

A abordagem correta é criar o objeto no escopo da sessão. Cada vez que um usuário loga, a aplicação gerencia uma nova instância de CarrinhoDeCompras associada exclusivamente àquele cliente (através de injeção de dependência ou ID de sessão).

Estudo de Caso Spark (Ex 4)
O código implementa contagem de dados em rede tolerante a falhas (mantém o estado vivo caso o driver caia). Funcionalidades que representam o ambiente de execução central costumam ser tratadas como Singletons no ambiente distribuído para economizar processamento e memória nos nós.

O padrão utilizado no método getInstance() é o Double-Checked Locking. Ele é thread-safe e otimizado. Não é desperdício: o primeiro if externo evita que a aplicação execute um comando de sincronização de thread (que é custoso) depois que a instância já existe. O segundo if garante que duas threads que passaram juntas pelo primeiro bloqueio não criem o objeto duas vezes.

Implementação Central de Alertas (Ex 5)
Ao desenvolver seu código em Java, a lógica central será baseada nestes requisitos:

Implementação: A CentralDeAlertas deve ter um construtor private, um atributo private static e um método público para retornar a instância.

Testes: Na classe Main, os objetos Policia, Samu e Bombeiros não podem usar new CentralDeAlertas(); eles devem usar o método estático para postar alertas, garantindo que o hashCode() exibido na tela seja idêntico para todos.

README: Justifique que múltiplas instâncias fariam os Bombeiros não enxergarem um alerta de incêndio postado pela Polícia. Para tornar a lista thread-safe em um cenário real com muitos chamados, sua classe deve usar o modificador synchronized nos métodos ou adotar listas concorrentes, como CopyOnWriteArrayList, evitando perda de dados simultâneos.