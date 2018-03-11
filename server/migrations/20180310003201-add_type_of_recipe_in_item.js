'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {

    return  [
      queryInterface.addColumn(
        'Items', 
        'Appetizer', 
        {
          type: Sequelize.Sequelize.BOOLEAN
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'Desserts', 
        {
          type: Sequelize.Sequelize.BOOLEAN
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'Salad', 
        {
          type: Sequelize.Sequelize.BOOLEAN
        //  allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'Soup', 
        {
          type: Sequelize.Sequelize.BOOLEAN
      //    allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'Fast-Food', 
        {
          type: Sequelize.Sequelize.BOOLEAN
        //  allowNull: true
        }
      )
    ];
  },
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.createTable('users', { id: Sequelize.INTEGER });
    */

  down: (queryInterface, Sequelize) => {
    return [
      queryInterface.removeColumn('Items', 'Appetizer'),
      queryInterface.removeColumn('Items', 'Desserts'),
      queryInterface.removeColumn('Items', 'Salad'),
      queryInterface.removeColumn('Items', 'Soup'),
      queryInterface.removeColumn('Items', 'Fast-Food')
    ];
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.dropTable('users');
    */
  }
};
