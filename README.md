# Power Distribution Engine - Backend
Welcome to Power Distribution Engine - Backend

[Final Report](https://docs.google.com/document/d/1waMC8Dy008sGkTmT8QTbn4Z5YRN4rB0j6Jkr6HAkVag/edit?usp=sharing)   
[Final Presentation](https://docs.google.com/presentation/d/18VR-pmh6YDRFnFCeVbwGybzk4kBMESyPSW89sCM0Lqk/edit?usp=sharing)
[Front-end Code](https://github.com/Agnique/react-alarm-engine.git)
[Links](https://docs.google.com/document/d/1yTTkP1E3tP8iVqp73hBIhuoGtjaZ4Is-NB16OYR9C54/edit?usp=sharing)
### Environment Setup
This project uses SpringBoot (maven as management tool) as well as Neo4j to setup programming and running environment
#### Java Version
Install Java 11.0.8 [Download Java 11.0.8](https://www.oracle.com/java/technologies/javase/11-0-8-relnotes.html "Download Java 11.0.8")
#### IntelliJ IDEA
The perfect IDE for Spring Boot Project.

Install the latest version [Download IntelliJ IDEA](https://www.jetbrains.com/idea/ "Download IntelliJ IDEA")

In the preference setup "plugins" in IntelliJ IDEA, be sure to install and enable "maven".

After setting up, please open the source folder in this git repository using IntelliJ IDEA, and it will automatically setting up Spring Boot environment managed by maven.

#### Neo4j Desktop
Graph database is used in this project.

Install the latest version [Download Neo4j Desktop](https://neo4j.com/download-neo4j-now/?utm_program=na-prospecting&utm_source=google&utm_medium=cpc&utm_campaign=na-search-branded&utm_adgroup=neo4j-desktop&gclid=CjwKCAiAz4b_BRBbEiwA5XlVVvf5xg2t1lUj7YwVR4IpB7RmGDsZ8E49aV0cC8hWK8aCamDJXCXhqxoC_LcQAvD_BwE "Download Neo4j Desktop")

After installing Neo4j descktop, create a new database with any name you like, be sure to select version "4.1.1" and set the password to "secret".

Click "start" to let the database run.
### DEMO
After correctly setting up the environment, click the run AlarmEngineApplication button or right click in the AlarmEngineApplication file, the system starts running.
You'll see stuff when you run front-end, and you can test APIs after importing a "model.json" file. (APIs are provided in the [final report](https://docs.google.com/document/d/1waMC8Dy008sGkTmT8QTbn4Z5YRN4rB0j6Jkr6HAkVag/edit?usp=sharing))
### Existing Problems
1. This project assumes single source power flow in the network, and might have problems in multi-source power system.
2. The current project doesn't provide alternative alarm and event message querying.
### Suggestions for Future Work
1. Multi-source path flow algorithms might be needed to find out.
2. Alarm messages could be queried through input, and should be working for front-end(especially for cause analysis).
