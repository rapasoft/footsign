/**
 * Created by cepe on 20.02.2015.
 */

var selectizeUserData;

// initialize comboboxes with players
function initSelect(rootUrl, names) {
    if (names) {

        if (selectizeUserData == undefined) {
            $.ajax({
                url: rootUrl + "/user_list",
                type: 'GET',
                dataType: 'json',
                async: false,
                success: function(data) {
                    selectizeUserData = data;
                }
            });
        }

        for(var i = 0; i < names.length; i++) {
            var name = names[i];
            makeSelectAlive(name, rootUrl, selectizeUserData)

        }
    }
}

function makeSelectAlive(name, rootUrl, data) {
    $(name).selectize({
        labelField: 'secondName',
        valueField: 'domainUserName',
        searchField: ['domainUserName', 'secondName', 'firstName'],
        options: data,
        hideSelected: true,
        render: {
            item: function (item, escape) {
                return '<div class="row" style="width: 100%">' +
                    '<div class="col-md-10">' +
                    '<span class="full-name">'+escape(item.secondName)+ ' ' + escape(item.firstName) + '</span>' +
                    '</div>'+
                    '<div class="col-md-2">' +
                    '<img class="icon" src="' + rootUrl + '/' + item.photoPath + '">' + '</img>' +
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
                    '<img class="icon" src="' + rootUrl + '/' + item.photoPath + '">' + '</img>' +
                    '</div>' +
                    '</div>';
            }

        }
    });

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

            $("#gameType").val(type)

        }

    }

}

function switchGamesButtons(isOver) {

    if (isOver == undefined) isOver = false;


    if (isOver) {
        $("#addNextRoundBtn").addClass("hidden");
        $("#saveGameBtn").removeClass("hidden");
    } else {
        $("#saveGameBtn").addClass("hidden");
        $("#addNextRoundBtn").removeClass("hidden");
    }
}

function showNextRound() {
    $("#addNextRoundBtn").addClass("disabled");

    if( $("#resultBlock2").hasClass("hidden") ) {
        $("#resultBlock2").removeClass("hidden");
        $($("#resultBlock2 input:first-child")[0]).focus();

        $("#resultBlock1 > div").addClass("has-success");
        $("#resultBlock1 input").attr("readonly", true);

        switchGamesButtons(false);
        return;
    }
    if( $("#resultBlock3").hasClass("hidden") ) {
        $("#resultBlock3").removeClass("hidden");
        $($("#resultBlock3 input:first-child")[0]).focus();

        $("#resultBlock2 > div").addClass("has-success");
        $("#resultBlock2 input").attr("readonly", true);

        switchGamesButtons(true);
    }
}


function isGoalsInputValid(value) {
    var val = parseInt(value);
    return !(isNaN(val) || val < 0 || val > 8);
}

function isGameInputValid(value1, value2) {
    var int1 = parseInt(value1);
    var int2 = parseInt(value2);

    return !(isNaN(int1) || isNaN(int2) || int1 == int2 || (int1 < 8 && int2 < 8) || int1 > 8 || int2 > 8);

}

function validateRoundInput() {
    if ( isGoalsInputValid($(this).val()) ) {
        $(this).parent().removeClass("has-error");
    } else {
        $(this).parent().addClass("has-error");
    }

    validAllGameInputs();
}

function validAllGameInputs() {
    var isValid = true;
    $(".roundResultInput").each(function() {
        if ($(this).is(":visible") && !isGoalsInputValid($(this).val())) {
            isValid = false;

        }
    });

    var games = $(".roundResultBlock:not(.hidden)");
    $(games).each(function (index) {
        var inputs = $(this).find("input");
        if (inputs.length == 2) {

            if (!isGameInputValid($(inputs[0]).val(), $(inputs[1]).val())) {
                isValid = false;
                $(inputs[0]).parent().addClass("has-error");
                $(inputs[1]).parent().addClass("has-error");
            } else {
                $(inputs[0]).parent().removeClass("has-error");
                $(inputs[1]).parent().removeClass("has-error");
            }
        }
    });

    if ( isValid ) {
        $("#addNextRoundBtn, #saveGameBtn").removeClass("disabled");
    } else {
        $("#addNextRoundBtn, #saveGameBtn").addClass("disabled");
    }
    return isValid;
}

function isGameOver(score1, score2) {
    if (score1 != undefined && score2 != undefined) {
        var s1 = parseInt(score1);
        var s2 = parseInt(score2);

        if (s1 == 2 || s2 == 2) {
            switchGamesButtons(true);
        } else {
            switchGamesButtons(false);
        }
    }
}

function checkMatchState() {
    var games = $(".roundResultBlock:not(.hidden)");
    var score1 = 0, score2 = 0;
    $(games).each(function (index) {
        var inputs = $(this).find("input");
        if (inputs.length == 2) {
            var val1 = parseInt($(inputs[0]).val());
            var val2 = parseInt($(inputs[1]).val());

            if (val1 > val2) {
                score1++;
            } else if (val2 > val1) {
                score2++
            }
        }

    });

    $(".team1Result").html(score1);
    $(".team2Result").html(score2);

    isGameOver(score1, score2);
}

function fillPlannedMatch(event) {
    if (event && event.data.matchId) {
        // set hidden input
        $("#matchId").val(event.data.matchId);
        
        // load teams in planned match
        var team1 = $("#planned_match_" + event.data.matchId + " .team1-player > div");
        var team2 = $("#planned_match_" + event.data.matchId + " .team2-player > div");
        
        // set combobox for each players in teams
        if (team1) {
            team1.each(function(index, data) {
                $("#selectLeft" + (index + 1))[0].selectize.setValue($(data).attr("data"));
            });
        }

        if (team2) {
            team2.each(function(index, data) {
                $("#selectRight" + (index + 1))[0].selectize.setValue($(data).attr("data"));
            });
        }
    }
    
}



// initialize components and binding events
$(document).ready(function () {
    var btn1 				= $(".game-type-1");
    var btn2			 	= $(".game-type-2");
    var btn3 				= $("#addNextRoundBtn");

    if (btn1) {
        btn1.on("click", {gameType: 1}, changeGameType);
    }
    if (btn2) {
        btn2.on("click", {gameType: 2}, changeGameType);
    }

    if(btn3) {
        btn3.on("click", showNextRound);
    }

    $(".fill-form-btn").each(function() {
        var matchId = $(this).attr("data");
        $(this).on("click", {matchId: matchId}, fillPlannedMatch);
    });

    $(".roundResultInput").change(checkMatchState).change(validateRoundInput);

    initStarsRating();

    $(".hide-content-btn").click(function () {
        $(this).parent().parent().parent().find("div.stats-table, div.stats-chart").slideToggle("slow");
    });

});

function initStarsRating(){
	var $starsRating = $('#stars_rating');
	$starsRating.on('rating.change', function () {
		document.getElementById('editForm').submit();
	});
	$starsRating.rating('refresh', {
        starCaptions: function(val) {
            if (val == 0) {
                return 'Not rated yet';
            } else if (val <= 2) {
                return 'Beginner';
            } else if (val <= 3) {
                return 'Casual Player';
            } else if (val <= 5) {
                return 'Intermediate amateur';
            } else if (val <= 7) {
                return 'Advanced amateur';
            } else if (val <= 9) {
                return 'Professional player';
            }
            else {
                return 'King :)';
            }
        },
        starCaptionClasses: function(val) {
            if (val == 0) {
                return 'label label-default';
            } else if (val <= 2) {
                return 'label label-danger';
            } else if (val <= 3) {
                return 'label label-warning';
            } else if (val <= 5) {
                return 'label label-info';
            } else if (val <= 7) {
                return 'label label-primary';
            } else if (val <= 9) {
                return 'label label-success';
            }
            else {
                return 'label label-success';
            }
        }
    });

}
