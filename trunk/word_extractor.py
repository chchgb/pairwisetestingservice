import sys
import codecs
import win32com
from win32com.client import Dispatch, constants

def extract_factor_levels_by_columns(table, separator=':'):
    columns = len(table.Columns)
    rows = len(table.Rows)
    factor_levels_list = []
    for column_index in range(columns):
        factor_levels = []
        for row_index in range(rows):
            # Get the cell value and remove last two srange beep chars
            value = table.Cell(row_index + 1, column_index + 1).Range.Text[:-2]
            # Ignore empty cell
            if value:
                factor_levels.append(value)

        # Output factor and levels
        factor_levels_list.append(separator.join(factor_levels))
        print separator.join(factor_levels)
    return factor_levels_list

def extract_factor_levels_by_rows(table, separator=':'):
    rows = len(table.Rows)
    factor_levels_list = []
    for row_index in range(rows):
        # Ignore the title
        if row_index == 0: continue

        factor_levels = []
        # Get the factor and levels
        factor = table.Cell(row_index + 1, 1).Range.Text[:-2]
        levels = table.Cell(row_index + 1, 2).Range.Text[:-2]
        factor_levels.append(factor)
        factor_levels.append(levels)

        # Output factor and levels
        factor_levels_list.append(separator.join(factor_levels))
        print separator.join(factor_levels)
    return factor_levels_list

def extract_factor_levels(table, separator=':'):
    if len(table.Columns) > 2:
        return extract_factor_levels_by_columns(table, separator)
    else:
        return extract_factor_levels_by_rows(table, separator)

filename = sys.argv[1]
separator = sys.argv[2]

msword = win32com.client.Dispatch('Word.Application')
msword.Visible = 0
msword.DisplayAlerts = 0

doc = msword.Documents.Open(FileName=filename)
try:
    factor_levels_list = extract_factor_levels(doc.Tables[0], separator)
    f = codecs.open("word_extractor_output.txt", 'w', 'utf-8')
    for factor_levels in factor_levels_list:
    	f.write(factor_levels)
        f.write("\n")
    f.close()
finally:
    doc.Close()
    # Not quit for speed
    # msword.Quit()
