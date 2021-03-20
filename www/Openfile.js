var exec = require('cordova/exec');
var PLUGIN_NAME = 'OpenFile';

var openfile = {
    open: function(filename, cb)
    {
        exec(cb, cb, 'OpenFile', 'open', [filename]);
    }
};

module.exports = openfile;