# PAAP-API
Repositório para configuração da API PAAP

Você precisara das seguintes tecnologias para rodar o servidor:
* Postgres 10+
* Java 11+

### Faça download do PostgreSQL em:  
https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

### Faça download do Java em:   
https://www.azul.com/downloads/zulu-community/?version=java-13&os=windows&architecture=x86-64-bit&package=jdk-fx

**Instalação Postgres**  
 Após a instalação do Wizard do programa, crie um banco com o nome de paap.
 
**Instalação do JAVA**  
  + Após o download, extraia os arquivos em uma pasta. Exemplo: C:\Program Files\Java;
  + Definir a variável de ambiente JAVA_HOME:  
      Digite variáveis de ambiente na barra de pesquisa do Windows e abra a opção 'editar variáveis de ambiente'.  
      Clique em variáveis de ambiente, na parte inferior de variáveis do sistema clique em novo.  
      Digite o nome da variável como JAVA_HOME e o valor é caminho da pasta que você extraiu. Exemplo: C:\Program Files\Java\zulu-jdk14
  + Instalar pacotes de extensão para JAVA Com IDE VSCode:   
      Vá em Extensions e instale o JAVA extension Pack. Com ele será possivel desenvolver Java no VSCode.  
      **Obs:** Essa é apenas um sugestão, sinta-se livre para utilizar qualquer IDE que você prefira. Só terá que pesquisar e atentar-se para as configurações adicionais em cada IDE com Java.
  + Abra o projeto (supondo que você está usando VSCode), e procure a classe principal 'CASaMovelApplication.java'. Clique na opção Run e o servidor irá subir.
