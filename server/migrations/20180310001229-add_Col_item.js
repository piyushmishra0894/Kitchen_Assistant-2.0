'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {

    return  [
      queryInterface.addColumn(
        'Items', 
        'timeToPrepare', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'calories', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'breakfast', 
        {
          type: Sequelize.Sequelize.INTEGER
        //  allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'dinner', 
        {
          type: Sequelize.Sequelize.INTEGER
      //    allowNull: true
        }
      ),
      queryInterface.addColumn(
        'Items', 
        'brunch', 
        {
          type: Sequelize.Sequelize.INTEGER
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
      queryInterface.removeColumn('Items', 'timeToPrepare'),
      queryInterface.removeColumn('Items', 'breakfast'),
      queryInterface.removeColumn('Items', 'calories'),
      queryInterface.removeColumn('Items', 'brunch'),
      queryInterface.removeColumn('Items', 'dinner')
    ];
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.dropTable('users');
    */
  }
};
