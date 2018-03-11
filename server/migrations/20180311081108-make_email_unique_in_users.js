'use strict';

module.exports = {
  up: (queryInterface, Sequelize) => {
    queryInterface.changeColumn(
      "Users",
      "email",
                    {
                        type      : Sequelize.STRING,
                        allowNull : false,
                        unique: true,
                    }
    )
    
    /*
      Add altering commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.createTable('users', { id: Sequelize.INTEGER });
    */
  },

  down: (queryInterface, Sequelize) => {
    queryInterface.changeColumn(
      "Users",
      "email",
                    {
                        type      : Sequelize.STRING,
                        allowNull : false,
                        unique: false,
                    }
    )
    /*
      Add reverting commands here.
      Return a promise to correctly handle asynchronicity.

      Example:
      return queryInterface.dropTable('users');
    */
  }
};
