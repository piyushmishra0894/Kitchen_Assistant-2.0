'use strict';
module.exports = (sequelize, DataTypes) => {
  var ItemIngredientMapping = sequelize.define('ItemIngredientMapping', {
    
  }, {});
  ItemIngredientMapping.associate = function(models) {
    ItemIngredientMapping.belongsTo(models.Items)
	/*, {
    	foreignKey: 'itemid',
     	onDelete: 'CASCADE',
    });*/
	ItemIngredientMapping.belongsTo(models.Ingredient)
	/*, {
    	foreignKey: 'ingredientid',
     	onDelete: 'CASCADE',
    });*/
  };
  return ItemIngredientMapping;
};