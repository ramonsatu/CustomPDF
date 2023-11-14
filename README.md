# CustomPDF
Este projeto contém uma implementação da classe PDFDocument do pacote android.graphics.pdf e da classe Paint do pacote android.graphics em conjunto com outros recursos
para gerar a grade de matérias a seguir:

![image](https://github.com/ramonsatu/CustomPDF/assets/117767174/0a944431-e34a-474b-8694-3c0fb73679de)

# Atenção!
Use este projeto como desejar. Contribuições são bem vindas.

# O necessário
O necessário para criar o seu primeiro PDF :

    private val pageHeight = 842
    private val pageWidth = 595
    private var gridPageNumber = 0
    private var pdfDocument: PdfDocument? = PdfDocument()
    private var paint: Paint? = Paint()
    private var line: Paint? = Paint()
    private lateinit var pageInfo: PdfDocument.PageInfo

Depois de alocar esses recusos dentro de uma classe , você pode alterar alguns atributos das isntâncias da Classe Paint dentro do bloco init ou dentro do escopo da função.

      init {
        paint?.flags = Paint.ANTI_ALIAS_FLAG
        line?.flags = Paint.ANTI_ALIAS_FLAG
        line?.strokeWidth = 2f
    }
               ------ or------
       private suspend fun generatePDFWeekSubjects(
        context: Context,
        activity: AppCompatActivity,
        subjectListSize: Int, paint: Paint,line:Paint
    ) {
          
        paint?.flags = Paint.ANTI_ALIAS_FLAG
        line?.flags = Paint.ANTI_ALIAS_FLAG
        line?.strokeWidth = 2f
        ...
    }    

# Configurando a página
Dentro do escopo da função responsável por conter a lógica estrutural do pdf nos vamos incializar a variável pagInfo, mas antes disso é preciso calcular o número de páginas que o seu pdf terá.
          
        gridPageNumber = (subjectListSize / 6) + 1

        //create page information
        pageInfo = PdfDocument.PageInfo.Builder(pageHeight, pageWidth, gridPageNumber).create()

    
Ao atribuir a chamada "PdfDocument.PageInfo.Builder(pageHeight, pageWidth, gridPageNumber).create()" a pagInfo é preciso repassar 3 valores INTEIROS como parâmetros do método "Builder".
Se o seu pdf tem um tamanho fixo, perfeito, basta inserir o valor direto. Mas se você trabalha com uma lista de tamanho n, é preciso fazer o seu cálculo para que o resultado sempre tenha um valor com pelo menos uma parte intera.

Se você estar se perguntando :
- Tudo bem , mas por qual motivo o tamanho da lista é dividido por 6 e somado a  1?

*Divido por 6?
Eu escolhi ter , no máximo, 6 matérias por página.

*Somado a 1?
Como a lista possui um tamanho n, poderiamos ter  n = 1. Logo, teriamos um valor que não possui uma parte inteira.
Ex:( 1 / 6 ) + 1 = 0.17 
        0.17 + 1 = 1.17 
No exemplo anterior eu granto que o builder intenda que o pdf possui pelo menos uma página.

em construção...


