# Custom Java HTTP Server & CRUD

Este projeto é um servidor HTTP desenvolvido do zero utilizando Java puro (Java SE), sem o uso de frameworks externos como Spring ou Jakarta EE. O objetivo principal foi explorar a implementação do protocolo HTTP, o gerenciamento de sockets, o roteamento de requisições e a serialização manual de dados.

# Tecnologias Utilizadas
- Linguagem: Java 17+

- Protocolo: HTTP/1.1

- Comunicação: Java Sockets (TCP)

- Formatos: JSON (Serialização manual)

# Arquitetura e Divisão de Pacotes
O projeto foi estruturado para separar as responsabilidades do "motor" do servidor das regras de negócio (CRUD).

**framework.server.http**: Contém o núcleo do servidor, incluindo o HttpParser para interpretar requisições e o gerenciamento de HttpRequest e HttpResponse. Além do gerenciamento dos Headers das requsições, tanto no controle de request quanto para as responses.

**framework.server.routing**: Responsável pelo roteamento com suporte a query parameters e path parameters. Utiliza um sistema de score para garantir o matching preciso, priorizando rotas estáticas sobre rotas dinâmicas.

**framework.server.json**: Implementação de lógica de conversão de objetos para JSON sem o uso de Reflection, garantindo performance e controle total sobre o output.

**crud.model**: Define as entidades de negócio (ex: **User**).

**crud.repository**: Responsável pela persistência de dados (**em memória**).

# Funcionalidades
-  **Servidor Multi-Threaded**: Capaz de lidar com múltiplas conexões simultâneas.

-  **Roteamento Dinâmico**: Suporte para os métodos GET, POST, PUT e DELETE.

-  **Serialização Manual**: Conversão de objetos Java para JSON implementada manualmente para evitar a sobrecarga de bibliotecas de reflexão.

-  **Gestão de Status Code**: Tratamento correto de respostas como 200 OK, 400 Bad Request, 404 Not Found.

# Testes Unitários

Para garantir a confiabilidade da serialização manual foram implementados testes unitários focados na classe JsonSerializer.

### Cobertura dos testes: 

- **Tipos Primitivos e Básicos**: Validação de serialização para String, Number (inteiros, float, double) e valores null.

- **Estruturas de Dados**: Verificação de integridade para coleções do tipo List e Map.

- **Objetos Customizados**: Teste específico para a entidade User, garantindo que o método toJson() do modelo está em conformidade com o serializador global.

## Exemplo de Resposta:

Para listar usuários em uma rota **GET /users**. Recebemos neste formato de resposta:

    ```
    {
        "id": 1,
        "nome": "Shin",
        "email": "nouzen@email.com"
    }
    ```


   