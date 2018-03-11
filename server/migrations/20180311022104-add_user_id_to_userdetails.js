'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {
    return  [
      queryInterface.addColumn(
        'UserDetails', 
        'UserID', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'UserDetails', 
        'FreqCookedRecipe', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'UserDetails', 
        'AvgCookingTime', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'UserDetails', 
        'AvgCaloriesPerMeal', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'UserDetails', 
        'FreqCuisine', 
        {
          type: Sequelize.Sequelize.STRING
          //allowNull: true
        }
      ),
      queryInterface.addColumn(
        'UserDetails', 
        'TotalRecipesCooked', 
        {
          type: Sequelize.Sequelize.INTEGER
          //allowNull: true
        }
      ),
      /*queryInterface.addConstraint('UserDetails', ['UserID'], {
        type: 'primary key',
        name: 'UserID',  
      }),*/
     /* queryInterface.addConstraint('UserDetails', ['UserID'], {
      type: 'foreign key',
      name: 'UserID',
      references: { //Required field
          table: 'Users',
          field: 'id'
        },
        onUpdate: 'cascade',
        onDelete: 'cascade'
      })*/
    ];
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.createTable('users', { id: Sequelize.INTEGER });
    */
  },

  down: (queryInterface, Sequelize) => {
    return [
      queryInterface.removeColumn('UserDetails', 'TotalRecipesCooked'),
      queryInterface.removeColumn('UserDetails', 'FreqCuisine'),
      queryInterface.removeColumn('UserDetails', 'AvgCaloriesPerMeal'),
      queryInterface.removeColumn('UserDetails', 'AvgCookingTime'),
      queryInterface.removeColumn('UserDetails', 'FreqCookedRecipe'),
      queryInterface.removeColumn('UserDetails', 'UserID')
    ];
    
    
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.dropTable('users');
    */
  }
};
