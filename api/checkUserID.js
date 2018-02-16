
module.exports = {
    //"get": function (req, res, next) {
    //}
    
    "get": function(req, res, next){
        var mysql      = require('mysql');
        var connection = mysql.createConnection({
          host     : 'kitchendb.ctztml3o8y0z.us-east-2.rds.amazonaws.com',
          user     : 'kitchenTeam',
          password : 'kitchenpassword',
          database : 'kitchenDB'
        });
        
        connection.connect();
        var name = req.param('name');
        var sql = 'SELECT * from USERS where name = "' + name +'"';
        connection.query('SELECT * from USERS where name = ?', [name] ,function (error, results, fields) {
          if (error) {
              res.status(200).type('application/json').send("SQL Error");
              throw error;
          }
          var date = { currentTime: Date.now() };
          console.log('The solution is: ', results[0].id);
          res.status(200).type('application/json').send(results[0]);
        });
        
        connection.end();
    }
}
