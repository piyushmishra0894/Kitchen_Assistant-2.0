'use strict';
module.exports = (sequelize, DataTypes) => {
  var Recipe = sequelize.define('Recipe', {
    Name: DataTypes.STRING,
    Step: DataTypes.STRING,
    Ingredient: DataTypes.STRING
  }, {});
  Recipe.associate = function(models) {
    // associations can be defined here
  };
  return Recipe;
};