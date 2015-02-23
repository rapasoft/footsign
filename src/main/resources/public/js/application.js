/**
 * Created by cepe on 20.02.2015.
 */
function initSelect(name) {
    if (name) {

        $(name).selectize({
            labelField: 'secondName',
            valueField: 'domainUserName',
            searchField: ['domainUserName', 'secondName', 'firstName'],
            remoteUrl: '/user_list',
            preload: true,
            load: function (query, callback) {
                $.ajax({
                    url: this.settings.remoteUrl,
                    type: 'GET',
                    dataType: 'json',
                    error: function() {
                        callback();
                    },
                    success: function(data) {
                        callback(data);
                    }
                });
            },
            render: {
                item: function (item, escape) {
                    return '<div class="row" style="width: 100%">' +
                                '<div class="col-md-10">' +
                                    '<span class="full-name">'+escape(item.secondName)+ ' ' + escape(item.firstName) + '</span>' +
                                '</div>'+
                                '<div class="col-md-2">' +
                                    '<img class="icon" src="../img/avatar.png">' + '</img>' +
                                '</div>' +
                            '</div>';
                },
                option: function(item, escape) {
                    return '<div class="row">' +
                                '<div class="col-md-10">' +
                                    '<span class="user">'+
                                        '<span class="full-name">'+escape(item.secondName)+ ' ' + escape(item.firstName) + '</span>' + ' ' +
                                        '<span class="domain-name">'+escape(item.domainUserName)+'</span>' +
                                    '</span>' +
                                    '<span>' +
                                        '<span class="department">' + escape(item.department) + '</span>' +
                                    '</span>' +
                                '</div>'+
                                '<div class="col-md-2">' +
                                    '<img class="icon" src="../img/avatar.png">' + '</img>' +
                                '</div>' + 
                            '</div>';
                }
                
            }
        });
    }
};
