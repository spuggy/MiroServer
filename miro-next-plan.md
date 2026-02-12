this project is an old web app written in java and using a postgres back end.   I would like to re write it in a next.js app.  I have created an empty next.js app in mironext.

Requirements for the new app:

1. use the exisitng db structure in create-tables.sql
2. use prismic as a db
3. use nextauth to create a login with the app_user table 
4. abandon the mr schema and have all the tables in one flat schema
5. list and paginate all the miroprojects for a user https://miro-assessment.com/mirotest/miroProjects.html
6. add a new miro project
6. view a new project and list all the candidates and allow a filter by name  https://miro-assessment.com/mirotest/showProject.html?id=2772
7. be able to add a new candidta evia a popup dialog
8. add a top menu bar My Projects Team Reports Help More
9. use MUI for the components
10. use prettier to format the code
11. add unit tests for any key logic
