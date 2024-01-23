## Sobre o projeto

* Microserviço construído com Java 17 + Spring. Consiste em um serviço simples de transações do tipo saque e depósito.
* O serviço possui duas entidades, sendo cliente e empresa; clientes podem realizar saques e depósitos nas empresas, não o contrário.
* A cada transação realizada é incidido uma taxa para empresa, tal taxa pode ser fornecida no corpo da requisição.
* Após cada transação, é enviado uma notificação para o e-mail do cliente informando o status de sucesso. Para testes será necessário inicializar um serviço de e-mail em algum provider de sua escolha e implementar no código, uma vez que esse e-mail deve estar vinculado ao provider do serviço de e-mail e no código onde solicita essa informação (foi utilizado o AWS SES para essa finalidade).
  * Dica: Se desejar utilizar um provider diferente, basta implementar a interface EmailSenderGateway na classe especifica do serviço de e-mail que deseja utilizar e criar outra classe para as configs, como as credenciais desse provider, eg. 
* Para empresa é realizado um callback com as informações da transação realizada. Foi utilizado o site https://webhook.site para simular um sistema externo.
* As entidades e os dados da transação são persistidas e recuperadas através do JPA. Foi utilizado o H2 database (banco em memória) para fins de testes.


## Instalação e execução

* Antes de executar será necessário clonar o projeto.
  ```
  git clone https://github.com/RaulMafra/transaction-service.git
  ```
### 1ª opção para execução:
* A primeira opção consiste em criar o pacote e executá-lo a partir dos passos abaixo:
   * Acesse o diretório do projeto e execute o comando abaixo para criar o pacote *.jar. 
      ```
      mvn package
      ```
    * Por fim, dentro do diretório do projeto, inicialize a aplicação a partir do *.jar criado.
      ```
      java -jar ./target/transaction-service-0.0.1-SNAPSHOT.jar
      ```
### 2ª opção para execução:
* A segunda opção é abrir o projeto em alguma IDE de sua escolha e inicializá-la a partir dela como qualquer outro projeto.


## Especificações

* A documentação está no formato OpenAPI 3.0, onde poderá ser acessada através do endereço http://localhost:8080/swagger-ui/index.html após a inicialização.
* Através da documentação será possível conhecer os endpoints, funcionalidade de cada um deles e realizar testes. Acredito que não cabe explicá-los aqui, uma vez que são auto-explicativos.
  
## License

Veja o arquivo [LICENSE](https://github.com/RaulMafra/transaction-service/blob/main/LICENSE) para direitos de licença e limitações (MIT).
