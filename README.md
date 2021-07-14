# SNACK CONTROLLER

Projeto para controle de funcionário responsável pela semana de aperitivos. O controle funciona como uma Queue(FIFO), onde cada funcionário é responsável por uma semana subsequente. Há algumas exclusões nessas regras que fazer a Queue mudar sua ordem:

1 - Um novo funcionário é responsável pelos aperitivos na semana seguinta a qual está o atual funcionário.

2 - Um funcionário pode conseguir Férias ou ficar Doente durante sua semana de responsabilidade, dando ao próximo funcionário sua vaga.

# Softwares

*[SpringBoot](https://spring.io/projects/spring-boot) - Framework utilizado.

*[Eclipse IDE](https://www.eclipse.org/downloads/) - IDE utilizada neste projeto.

*[Maven](https://maven.apache.org/) - Controlador de dependências.

# Versões e Dependências

*[Data JPA](https://spring.io/projects/spring-data-jpa) - Versão 2.5.2

*[Validation](https://mvnrepository.com/artifact/javax.validation/validation-api) - Versão 2.5.2

*[Lombok](https://projectlombok.org/) - Versão 1.18.20

*[H2 Databse](https://www.h2database.com/html/main.html) - Versão 1.4.2

*[JUnit 5 - Jupiter](https://junit.org/junit5/) - Versão 2.5.2

# End-points

Os end-points criados foram:

FUNCIONÁRIOS:
- (GET)    /employee             - Retorna a lista completa de funcionários
- (GET)    /employee/present     - Retorna a lista de funcionários que não estão ausentes(Férias/Doença)
- (GET)    /employee/ausent      - Retorna a lista de funcionários ausentes
- (POST)   /employee             - Necessário um form de cadastro para cadastrar um novo funcionário
- (PUT)    /employee/{id}        - Necessário um form para atualização do funcionário dono do ID passado 
- (DELETE) /employee/{id}        - Deleta o funcionário dono do ID passado  

SNACKSWORK:
- (GET)    /work                 - Retorna a lista de trabalhos completa
- (GET)    /work/actual          - Retorna o trabalho atual.
- (GET)    /work/start           - Inicia o primeiro fucnionário na sequência de trabalho com o trabalho da atual semana
- (PUT)    /work/ausent/{id}     - Inverte o status isAusent do funcionario dono do ID passado, atualizando o trabalho caso necessario 
- (GET)    /work/update          - Faz a solicitação de atualização do trabalho atual (É atualizado case tenha passsado 7 dias ou funcionario estiver doente)        

# A melhorar

Seguem abaixo pontos a melhorar:

- Abstrair regras de negócio utilizada na classe EmployeeService para uma classe dedicada.
- Refatorar condições para Strategy Pattern.
- Aumentar a Cobertura de Código com testes Unitários e Integrados.
- Incluir Autenticação com Spring Security.
- Incluir Swagger para documentação.
- Incluir Persistência de dados com MySQL, Postgree ou qualquer outro tipo de banco de dados.
- Gerar uma imagem Docker para solucionar problemas de deploy.