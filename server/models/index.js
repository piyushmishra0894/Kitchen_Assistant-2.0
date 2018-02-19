'use strict';

var fs        = require('fs');
var path      = require('path');
var Sequelize = require('sequelize');
var basename  = path.basename(__filename);
var env       = process.env.NODE_ENV || 'development';
var config    = require(__dirname + '/../config/config.json')[env];
var db        = {};
const Umzug = require('umzug');

if (config.use_env_variable) {
  var sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  var sequelize = new Sequelize(config.database, config.username, config.password, config);
}

fs
  .readdirSync(__dirname)
  .filter(file => {
    return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js');
  })
  .forEach(file => {
    var model = sequelize['import'](path.join(__dirname, file));
    db[model.name] = model;
  });

Object.keys(db).forEach(modelName => {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;

const umzug = new Umzug({
  storage: 'sequelize',
  storageOptions: {
      sequelize: sequelize,
  },

  // see: https://github.com/sequelize/umzug/issues/17
  migrations: {
      params: [
          sequelize.getQueryInterface(), // queryInterface
          sequelize.constructor, // DataTypes
          function() {
              throw new Error('Migration tried to use old style "done" callback. Please upgrade to "umzug" and return a promise instead.');
          }
      ],
      path: './server/migrations',
      pattern: /\.js$/
  },

  logging: function() {
      console.log.apply(null, arguments);
  },
});

function cmdMigrate() {
  return umzug.up();
}

function cmdReset() {
  return umzug.down({ to: 0 });
}

cmdMigrate();

