/**
 * Created by cepe on 20.02.2015.
 */
function initSelect(name) {
    if (name) {

        $(name).selectize({
            sortField: 'text'

        });
    }
};
