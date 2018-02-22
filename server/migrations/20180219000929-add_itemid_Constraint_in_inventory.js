'use strict';

module.exports = {
 up: (queryInterface, Sequelize) => {
   return  queryInterface.addConstraint('Inventories', ['Itemid'], {
      type: 'foreign key',
      name: 'Itemid',
      references: { //Required field
          table: 'Ingredients',
          field: 'id'
  },
  onDelete: 'cascade',
  onUpdate: 'cascade'
});
  },

  down: (queryInterface, Sequelize) => {
    return queryInterface.dropTable('Inventories');
  }
};
