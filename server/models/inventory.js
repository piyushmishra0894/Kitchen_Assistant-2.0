'use strict';
module.exports = (sequelize, DataTypes) => {
  var Inventory = sequelize.define('Inventory', {
    UserId: DataTypes.INTEGER,
    Itemid: DataTypes.INTEGER,
    Quantity: DataTypes.INTEGER
  }, {});
  Inventory.associate = function(models) {
    // associations can be defined here
    Inventory.belongsTo(models.User)
    /*, {
    	foreignKey: 'userId',
     	onDelete: 'CASCADE',
    });*/
    Inventory.belongsTo(models.Ingredient)
    /*, {
    	foreignKey: 'ingredientId',
     	onDelete: 'CASCADE',
    });*/
  };
  return Inventory;
};