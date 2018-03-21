

# Kitchen_Assistant



* To start server:
  1. install nodemon npm package -  'npm i -D nodemon'
  2. edit script section in package.json 
  "scripts": {
  "start:dev": "nodemon ./bin/www",
  "test": "echo \"Error: no test specified\" && exit 1"
},
3. run application using following command  - npm run start:dev


* We are using Sequelize ORM. Following are the steps to install and use sequelize to create models and migration files:
  1. install sequelize cli and sequelize for mysql
      npm install -g sequelize-cli
      npm install --save sequelize
      npm install --save mysql2
  2. add paths to different folders(ex migration files, models)in  .sequelizerc file in project's root folder 
  3. run command 'sequelize init' to generate directories and boilerplate code as specified in .sequelizerc.
 
* add database details in server/config.json file.

For developement :
* to create a new migration run following command:
  sequelize migration:generate --name [name_of_your_migration]
* to create new model, run following command:
  sequelize model:generate --name [model name] --attributes [attribute1]:[attribute1 type], [attribute2]:[attribute2 type]
* add routes in file server/routes/index.js


Deployment:
Android application deployment steps:
1. Open the project in Android Studio.
2. Go to Build->Clean Project.
3. Go to Build->Rebuild Project

The above steps ensure that there are no errors in the imported project.
4. Go to Build->Generate Signed APK…
5. Create a new keystore or give the path to an existing keystore.
Note: If creating a new keystore, remember the password provided.
6. Fill in the rest of the details and click on Next.
7. Choose appropriate APK Destination folder.
8. Select Build type as Release.
9. Choose V2 (Full APK Signature).
10. Click on Finish.

The APK will now be available for distribution at your chosen destination folder.
While installing the APK on your Android phone, the following steps will need to be performed:

1. Navigate to Setting > Security.
2. Check the option "Unknown sources".
3. Tap OK on the prompt message.
4. Select "Trust".

      




