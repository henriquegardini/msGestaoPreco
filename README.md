# Microserviço responsável pela parte de gestão de preços do sistema.

## Descrição

O projeto **msGestaoPreco** é um microserviço dedicado à gestão de preços. Desenvolvido usando o framework Spring Boot, o sistema fornece funcionalidades para gerenciar preços de itens, incluindo a capacidade de cadastrar, atualizar, obter e excluir preços.

## Funcionalidades

- **Cadastro e Atualização de Preços:** Cadastro de novos preços para itens e atualização de preços existentes, incluindo ajustes de preço normal e promocional, além de datas de início e fim de promoção.
- **Obtenção de Preços:** Recuperação do preço atual de um item com base no identificador do item e fornecimento de uma lista de todos os preços cadastrados no sistema.
- **Exclusão de Preços:** Exclusão do preço associado a um item específico, com confirmação de sucesso e mensagem de exclusão.
- **Cálculo de Preço Atual:** Cálculo e retorno do preço atual do item, considerando a vigência de promoções com base na data atual.
- **Gestão de Exceções:** Tratamento de erros utilizando exceções personalizadas, como ItemNotFoundException, para fornecer mensagens claras e informativas sobre a ocorrência de erros.

## Requisitos

- Java 17+
- Spring Boot 3.x
- Maven 3.x

## Estrutura do Projeto

- **Controller:** Camada responsável por gerenciar as requisições HTTP relacionadas a preços, incluindo operações de cadastro, atualização, obtenção e exclusão de preços.
- **Service:** Camada onde está a lógica de negócio para o gerenciamento de preços, incluindo validação de dados de preços e cálculo do preço atual unitário.
- **Repository:** Camada de acesso a dados utilizando Spring Data JPA, responsável pela persistência e recuperação de informações sobre preços no banco de dados.
- **Exception Handling:** Implementação de tratamento de exceções personalizadas, como ItemNotFoundException, para fornecer mensagens claras e informativas sobre erros relacionados à gestão de preços.
- **Model:** Camada que define a estrutura de dados para a entidade Preco, incluindo atributos como preço normal, preço promocional e datas de promoção.
- **Batch:**  Camada responsável pelo processamento em batch de dados, utilizando Spring Batch para carregar e processar grandes volumes de dados de preços a partir de arquivos CSV para o banco de dados.

## Configuração

1. Clone o repositório:

   ```bash
   git clone https://github.com/henriquegardini/msGestaoPreco.git
    ```

2. Accesse o diretório do projeto:

   ```bash
   cd msGestaoPreco
   ```

3. Instale as dependências do Maven:

   ```bash
   mvn clean install
   ```

4. Configure o banco de dados no arquivo application.properties ou application.yml.

5. Execute a aplicação:

   ```bash
   mvn spring-boot:run
   ```

## Uso

### Endpoints disponíveis:

- **POST /precos/precificar**

  Cadastro ou atualização de preços para um item. Recebe um objeto `PrecoDto` no corpo da requisição e retorna o preço atualizado.

- **GET /precos/obterPreco/{itemId}**

  Recupera o preço atual de um item específico com base no identificador do item. Retorna o preço no formato `PrecoDto` .

- **GET /precos/obterPrecos**

  Recupera a lista de todos os preços cadastrados no sistema. Retorna uma lista de objetos `PrecoDto` .

- **DELETE /precos/excluirPreco/{itemId}**

  Exclui o preço associado a um item específico com base no identificador do item. Retorna uma mensagem de sucesso confirmando a exclusão do preço.


### Exemplo de Requisição:
**Cadastrar ou atualizar preco do item**:
```bash
  curl -X POST http://localhost:8080/precos/precificar \
     -H "Content-Type: application/json" \
     -d '{
           "itemId": 99,
           "precoNormal": 100.00,
           "precoPromocional": 80.00,
           "dataInicioPromocao": "2024-09-01",
           "dataFimPromocao": "2024-09-30",
           "precoAtualUnitario": 80.00
         }'
```

**Obter preco Item**:
```bash
curl -X GET http://localhost:8080/precos/obterPreco/99
```

**Obter precos**:
```bash
curl -X GET http://localhost:8080/precos/obterPrecos
```


**Deletar preco Item**:
```bash
curl -X DELETE http://localhost:8080/precos/excluirPreco/99
```
