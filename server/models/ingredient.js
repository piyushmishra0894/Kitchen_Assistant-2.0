'use strict';
module.exports = (sequelize, DataTypes) => {
  var Ingredient = sequelize.define('Ingredient', {
    name: DataTypes.STRING,
    measurementType: DataTypes.STRING
  }, {});
  Ingredient.associate = function(models) {
    // associations can be defined here
  };
  return Ingredient;
};