'use strict';

module.exports = {
 up: (queryInterface, Sequelize) => {
   return  queryInterface.addConstraint('Inventories', ['Userid'], {
      type: 'foreign key',
      name: 'Userid',
      references: { //Required field
          table: 'Users',
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
