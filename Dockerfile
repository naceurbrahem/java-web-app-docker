FROM tomcat:9-jre11-slim
# Supprime les applications par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*
# Copie le fichier généré par Maven dans le dossier de déploiement de Tomcat
COPY target/javawebapp.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]