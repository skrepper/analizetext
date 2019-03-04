# analizetext

 Дан файл, например
 summer -> autumn
 
 summer && winter -> rain
 
 summer && winter -> uragan
 
 summer || spring -> dump
 
 summer || rain -> snow
 
 dump || rain -> mud
 
 ----------------------------
 
 summer, autumn, rain, not_in_upper_text

Программе на вход подается название файла.
Необходимо вывести строку, в которой указаны определенные в нижней строке переменные, 
а также переменные, которые получили определение, с учетом булевых операций.
То есть uragan не должен попасть в вывод
