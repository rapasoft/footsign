/**
 * Created by cepe on 20.02.2015.
 */


// initialize comboboxes with players
function initSelect(rootUrl, name) {
    if (name) {

        $(name).selectize({
            labelField: 'secondName',
            valueField: 'domainUserName',
            searchField: ['domainUserName', 'secondName', 'firstName'],
			remoteUrl: rootUrl + "/user_list",
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
						'<img class="icon" src="' + rootUrl + '/img/avatar.png">' + '</img>' +
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
						'<img class="icon" src="' + rootUrl + '/img/avatar.png">' + '</img>' +
                                '</div>' + 
                            '</div>';
                }
                
            }
        });
    }
}

function changeGameType(event) {
    if( event && event.data.gameType) {
        
        var type = event.data.gameType;
        
        if (!isNaN(type) && ( type == 1 || type == 2 )) {

            if (type == 1) {
                // hide and unselect selected option
                $(".second-player").addClass("hidden").find("option").attr("selected", false);
                $(".game-type-1").addClass("active");
                $(".game-type-2").removeClass("active");
            } else {
                // show and select first option
                $(".second-player").removeClass("hidden").find("option").attr("selected", "selected");
                $(".game-type-2").addClass("active");
                $(".game-type-1").removeClass("active");
            }

        }

    }
    
}

function showNextRound() {
    if( $("#resultBlock2").hasClass("hidden") ) {
        $("#resultBlock2").removeClass("hidden");

        $("#resultBlock1 > div").addClass("has-success");
        $("#resultBlock1 input").attr("readonly", true);
        return;
    }
    if( $("#resultBlock3").hasClass("hidden") ) {
        $("#resultBlock3").removeClass("hidden");

        $("#resultBlock2 > div").addClass("has-success");
        $("#resultBlock2 input").attr("readonly", true);
        
        $("#addNextRoundBtn").addClass("hidden");
        $("#saveGameBtn").removeClass("hidden")
    }
}

function validateRoundInput() {
    var value = parseInt($(this).val());
    if (isNaN(value) || value < 0 || value > 8) {
        $(this).parent().addClass("has-error");
    } else {
        $(this).parent().removeClass("has-error");
    }
    
}

function saveGame() {
    
    
}

// initialize components and binding events
$(document).ready(function () {
    var btn1 = $(".game-type-1");
    var btn2 = $(".game-type-2");
    var btn3 = $("#addNextRoundBtn");
    var btn4 = $("#addNextRoundBtn");

    if (btn1) {
        btn1.on("click", {gameType: 1}, changeGameType);
    }
    if (btn2) {
        btn2.on("click", {gameType: 2}, changeGameType);
    }
    
    if(btn3) {
        btn3.on("click", showNextRound);
    }
    
    if (btn4) {
        btn4.on("click", saveGame)
        
    }
    
    $(".roundResultInput").change(validateRoundInput)
});
