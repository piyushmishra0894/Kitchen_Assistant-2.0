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

/*
    return queryInterface.createTable('Inventories', {
     
      id: {
        allowNull: false,
        autoIncrement: true,
        primaryKey: true,
        type: Sequelize.INTEGER
      },
      userId: {
        type: Sequelize.INTEGER,
          references: {
            model: 'users',
            key: 'id'
          },
          allowNull: false
      },
      ingredientId: {
        type: Sequelize.INTEGER,
          references: {
            model: 'ingredients',
            key: 'id'
          },
          allowNull: false
      },
      Quantity: {
        type: Sequelize.INTEGER
      },
      createdAt: {
        allowNull: false,
        type: Sequelize.DATE
      },
      updatedAt: {
        allowNull: false,
        type: Sequelize.DATE
      }
    });
  },*/
  down: (queryInterface, Sequelize) => {
    return queryInterface.dropTable('Inventories');
  }
};
