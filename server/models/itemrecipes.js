'use strict';
module.exports = (sequelize, DataTypes) => {
  var ItemRecipes = sequelize.define('ItemRecipes', {
    StepName: DataTypes.STRING,
    StepNumber: DataTypes.INTEGER,
	StepDescription: DataTypes.STRING
  }, {});
  ItemRecipes.associate = function(models) {
    ItemRecipes.belongsTo(models.Items)
	/*, {
    	foreignKey: 'itemid',
     	onDelete: 'CASCADE',
    });*/
  };
  return ItemRecipes;
};