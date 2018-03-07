'use strict';
module.exports = (sequelize, DataTypes) => {
  var Items = sequelize.define('Items', {
    Name: DataTypes.STRING
  }, {});
  Items.associate = function(models) {
    
  };
  return Items;
};