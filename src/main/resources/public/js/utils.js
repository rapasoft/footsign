/**
 * Created by cepe on 17.03.2015.
 */

function getFormattedDate(date, format) {
    if (date) {
        if(format == undefined) {
            format = "dd.MM.yyyy";
        }
        return $.datepicker.formatDate(format, date);
    }
    return "";
}