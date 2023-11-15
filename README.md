# CustomPDF
Este projeto contem uma implementação da classe PDFDocument do pacote android.graphics.pdf e da classe Paint do pacote android.graphics em conjunto com outros recursos
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
Dentro do escopo da função responsável por conter a lógica estrutural do pdf nos vamos incializar a variável pagInfo, mas antes disso é preciso calcular o número de páginas que o seu PDF terá.
          
        gridPageNumber = (subjectListSize / 6) + 1

        //create page information
        pageInfo = PdfDocument.PageInfo.Builder(pageHeight, pageWidth, gridPageNumber).create()

    
Ao atribuir a chamada "PdfDocument.PageInfo.Builder(pageHeight, pageWidth, gridPageNumber).create()" a pagInfo é preciso repassar 3 valores INTEIROS como parâmetros para o método "Builder".
Se o seu PDF tem um tamanho fixo, perfeito, basta inserir o valor direto. Mas se você trabalha com uma lista de tamanho igual a n, é preciso fazer o seu cálculo para que o resultado sempre tenha um valor com pelo menos uma parte intera.

Se você estar se perguntando :

  -Tudo bem , mas por qual motivo o tamanho da lista é dividido por 6 e somado a  1?

Divido por 6?

Foi definido ter , no máximo, 6 linhas na grade por página.

Somado a 1?

Como a lista possui um tamanho n, poderiamos ter  n = 1. Logo, teriamos um valor que não possui uma parte inteira.

Ex:

( 1 / 6 ) = 0.17 
    
0.17 + 1 = 1.17 
        
No exemplo anterior, garantimos que o builder vai criar, pelo menos, uma página para o PDF.

Se o tamanho for 6?

Ex:

( 6 / 6 ) = 1  

  -Tudo certo, então?

Não. Lembra da imagem no início? Nela só cabem 5 linhas na grade. Fica sobrando uma linha. 

O que faremos? 
    
( 6 / 6 ) = 1
   
   1 + 1  = 2

Agora fica claro que somando 1 ao resultado da divisão, garantimos que as linhas excedentes estarão na próxima página.    


# Preenchendo as páginas
Agora que defimos como passar o número de páginas para o "PdfDocument.PageInfo.Builder()", vamos utilizá-lo para definir o números de iterações que termos na primeira estrutura de laço.

      for (indexPageNumber in 0 until gridPageNumber step 1) {
            //set page information
            val myPage: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)
            // creating a variable for canvas
            val canvas = myPage.canvas
            ...
      }

Perceba que no início de cada iteração, iniciamos "myPage" com a instância do PDFDocument que criamos e passamos as configurações da página.
           
            val myPage: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)

Logo em seguida temos acesso ao objeto canvas e aos métodos responsáveis por desenhar na tela.

      val canvas = myPage.canvas

Métodos:

     //Desenha um bitmap
     canvas.drawBitmap(bitmap, left, top, paint)

     //Desenha textos
     canvas.drawText(text, x, y, paint)

     //Desenha linhas
     canvas.drawLine(start x, start y, stop x, stop y, paint)
     
Dentro da nossa primeira estrutura condicional nos desenhamos o cabeçalho do PDF:

             //--------page-01-Header------------
            if (indexPageNumber == 0) {
                canvas.drawBitmap(bitmapLogoScaled, 25f, 40f, paint)

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 18F
                paint.color =
                    ContextCompat.getColor(context, R.color.black)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(tosPhrase, 104f, 60f, paint)
                ...
            }

Após passamos de forma senquencial pela estrutura anterior chegamos no laço aninhado que contem a lógica que desenha a tabela.

            //-------------Table----------------
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textAlign = Paint.Align.CENTER
            paint.color =
                ContextCompat.getColor(context, R.color.black)

            for (index in 0 until subjectListSize step 1) {

                //First page
                if ((indexPageNumber == 0) && (index < 5)) {
                    ...
                }
                //Other pages, 2...3..100...
                if ((indexPageNumber == indexOnThePage) && (index in indexMin..indexMax)) {
                     ...
                }
            }
O impotante é ter em mente a lógica da ordenação das estruturas de repetição e de condição.
No final da estrutura do laço principal você deve fechar cada página.

            for (indexPageNumber in 0 until gridPageNumber step 1) {

            //set page information
            val myPage: PdfDocument.Page = pdfDocument!!.startPage(pageInfo)
            // creating a variable for canvas
            val canvas = myPage.canvas

            //--------page-01-Header------------
            if (indexPageNumber == 0) {
                ...
            }
            //-------------Table----------------
            for (index in 0 until subjectListSize step 1) {
                //First page
                if ((indexPageNumber == 0) && (index < 5)) {
                   ...
                }
                //Other pages, 2...3..100...
                if ((indexPageNumber == indexOnThePage) && (index in indexMin..indexMax)) {
                   ...
                }      
            }
            pdfDocument!!.finishPage(myPage)
        }

# Definindo o Path
Agora que já temos as páginas preenchidas , precisamos informar para instância do PDfDocument o caminho onde o nosso arquivo deve ser escrito.

    for (indexPageNumber in 0 until gridPageNumber step 1) {
         ...
        }
        // Nome do arquivo pdf
        val pdfFileName = context.getString(R.string.tos_blank_subject_grid)
        
        // public File(File parent, String child )
        val filePDF = File(getPathFile("/TOS-PDFs", context), pdfFileName)

        try {
            withContext(Dispatchers.IO) {
                pdfDocument!!.writeTo(FileOutputStream(filePDF))
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }  
        
Ao atribuimos "File(getPathFile("/TOS-PDFs", context), pdfFileName)" a filePDF, estamos definindo um caminho específico para o arquivo.
Na estrutura try{}catch{}, estamos acessando o método responsável por escrever o nosso arquivo no caminho que foi definido dentro da função "getPathFile("/TOS-PDFs", context)".
Quando a operação do metódo "writeTo(FileOutputStream(filePDF))" acabar, devemos fechar a instância do PDFDocument e desalocar os recursos.

            for (indexPageNumber in 0 until gridPageNumber step 1) {
         ...
        }
        // Nome do arquivo pdf
        val pdfFileName = context.getString(R.string.tos_blank_subject_grid)
        
        // public File(File parent, String child )
        val filePDF = File(getPathFile("/TOS-PDFs", context), pdfFileName)

        try {
            withContext(Dispatchers.IO) {
                pdfDocument!!.writeTo(FileOutputStream(filePDF))
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } 

        pdfDocument!!.close()
        pdfDocument = null
        this.paint = null
        this.line = null

        insertFileInMediaStore(filePDF, pdfFileName, context)

        goToFileDirectory(activity)


    }

# Fun insertFileInMediaStore(filePDF, pdfFileName, context)
Esta função é resposável por inserir o arquivo gerado no MediaStore dependedo da versão do android.

# Fun goToFileDirectory(context, activity)
Esta função redireciona o usuário para pasta Download, caso haja algum recurso capaz de lidar com a Intent.

# Preciso de permissões?
Dependendo da versão da API do android você deve solicitar ao usuário as permissões a seguir:

                Manifest.permission.READ_EXTERNAL_STORAGE
                Manifest.permission.WRITE_EXTERNAL_STORAGE

Dentro do projeto existe este tratamento, caso queira explorar.
