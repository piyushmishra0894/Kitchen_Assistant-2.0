'use strict';
module.exports = (sequelize, DataTypes) => {
  var UserDetails = sequelize.define('UserDetails', {
    Age: DataTypes.INTEGER,
    Sex: DataTypes.STRING,
    Nationality: DataTypes.STRING,
    CurrentCity: DataTypes.STRING,
    CurrentCountry: DataTypes.STRING,
    UserID: DataTypes.INTEGER,
    FreqCookedRecipe: DataTypes.INTEGER,
    AvgCookingTime: DataTypes.INTEGER,
    AvgCaloriesPerMeal: DataTypes.INTEGER,
    FreqCuisine: DataTypes.STRING,
    TotalRecipesCooked: DataTypes.INTEGER
  }, {});
  UserDetails.associate = function(models) {
    UserDetails.belongsTo(models.User)
    // associations can be defined here
  };
  return UserDetails;
};