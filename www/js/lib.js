/*
 * Copyright (C) 2020 Ivan Kniazkov
 *
 * This file is part of Antcore.
 *
 * Antcore is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Antcore is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Antcore.
 * If not, see <http://www.gnu.org/licenses/>.
 */

var xmlHttpObject = null;
var server = window.location.protocol + '//' + window.location.host;

var getXmlHttp = function()
{
    if (xmlHttpObject)
        return xmlHttpObject;

    try {
        xmlHttpObject = new ActiveXObject('Msxml2.XMLHTTP');
    }
    catch (e0) {
        try {
            xmlHttpObject = new ActiveXObject('Microsoft.XMLHTTP');
        }
        catch (e1) {
            xmlHttpObject = false;
        }
    }
    if (!xmlHttpObject && typeof XMLHttpRequest!='undefined')
        xmlHttpObject = new XMLHttpRequest();
    return xmlHttpObject;
};

var sendRequest = function(request, callback) {
    var req = getXmlHttp()
    req.open('GET', server + '?request=' + encodeURIComponent(request), true);
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
            if(req.status == 200) {
                callback(req.responseText);
            }
        }
    };
    req.send(null);
};

var sendRequestWithData = function(request, data, callback)
{
    var req = getXmlHttp();
    req.open('GET', server + '?request=' + encodeURIComponent(request) + '&data=' + encodeURIComponent(JSON.stringify(data)), true);
    req.onreadystatechange = function() {
        if (req.readyState == 4) {
            if(req.status == 200) {
                callback(req.responseText);
            }
        }
    };
    req.send(null);
};

var addEvent = function(object, type, callback)
{
    if (typeof(object) == "string")
        object = document.getElementById(object);
    if (object == null || typeof(object) == "undefined")
        return;

    if (object.addEventListener)
        object.addEventListener(type, callback, false);
    else if (object.attachEvent)
        object.attachEvent("on" + type, callback);
    else
        object["on" + type] = callback;
};

var urlParameters = null;

var getUrlParameters = function() {
    if (urlParameters)
        return urlParameters;

    urlParameters = { };
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    var i;
    for (i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        var key = decodeURIComponent(pair[0]);
        var value = decodeURIComponent(pair[1]);
        urlParameters[key] = value;
    }
    return urlParameters;
};

var formatString = function(format) {
    var result = format;
    for (var i = 1; i < arguments.length; i++)
    {
        result = result.replace('{' + (i - 1) + '}', arguments[i]);
    }
    return result;
};

var escapeHtml = function(unsafe)
{
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
};
