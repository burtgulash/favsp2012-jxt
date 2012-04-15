<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
<xsl:output method="html"/>
	
<xsl:variable name="this" select="/" />
<xsl:variable name="data" select="document('data.xml')" />

<xsl:template name="zaSemestr">
	<xsl:param name="semestr" />
	<semestr>
		<zimniLetni><xsl:value-of select="$semestr" /></zimniLetni>
		<xsl:for-each select="$data/data/dny/den">
			<xsl:variable name="den" select="." />
			<radka>
				<den><xsl:value-of select="." /></den>
				<xsl:for-each select="$this/student/predmet[semestr = $semestr
													and den = $den]">
					<xsl:sort select="zacatek" />
					<xsl:copy-of select="." />
				</xsl:for-each>
			</radka>
		</xsl:for-each>
	</semestr>
</xsl:template>


<xsl:template match="/" mode="faze1">
	<rozvrh>
		<xsl:call-template name="zaSemestr">
			<xsl:with-param name="semestr" select="'ZS'" />
		</xsl:call-template>
		<xsl:call-template name="zaSemestr">
			<xsl:with-param name="semestr" select="'LS'" />
		</xsl:call-template>
	</rozvrh>
</xsl:template>


<xsl:template match="*|/" mode="faze2">
	<xsl:copy>
		<xsl:apply-templates mode="faze2" />
	</xsl:copy>
</xsl:template>

<xsl:template match="radka" mode="faze2">
	<xsl:variable name="radka" select="." />

	<!-- Vypočítat maximum kolizí pro daný den. -->
	<xsl:variable name="maxKolizi">
		<xsl:for-each select="$data/data/casy/hodina">
				<xsl:sort data-type="number" order="descending"
	select="count($radka/predmet[xs:time(zacatek) &lt;= xs:time(current()/zacatek)
						and xs:time(konec) &gt;= xs:time(current()/konec)])" />
			<xsl:if test="position() = 1">
				<xsl:value-of
	select="count($radka/predmet[xs:time(zacatek) &lt;= xs:time(current()/zacatek)
					and xs:time(konec) &gt;= xs:time(current()/konec)])" />

			</xsl:if>
		</xsl:for-each>
	</xsl:variable>

	<faze2den>
		<rowspan><xsl:value-of select="$maxKolizi" /></rowspan>
		<xsl:variable name="predmety" select="predmet" />
		<xsl:apply-templates select="den" mode="faze2" />
		<xsl:for-each select="1 to $maxKolizi">
			<xsl:variable name="i" select="." />
			<faze2radka>
				<!-- Pro každou řádku vybrat ty předměty, které mají $i
					 předcházejících předmětů konfliktních. -->
				<xsl:for-each select=
	"for $p in $predmety
		return
			if (count(
					$predmety[xs:time(zacatek) &lt;= xs:time($p/zacatek)
						and xs:time(konec) &gt;= xs:time($p/zacatek)]) = $i)
				then $p
				else ()">
					<xsl:copy-of select="." />
				</xsl:for-each>
			</faze2radka>
		</xsl:for-each>

		<!-- Žádný řádek se nevytvoří, ručně ho dodělat. -->
		<xsl:if test="$maxKolizi = 0">
			<faze2radka />
		</xsl:if>
	</faze2den>
</xsl:template>
	


<xsl:template match="*|/" mode="faze3">
	<xsl:copy>
		<xsl:apply-templates mode="faze3" />
	</xsl:copy>
</xsl:template>

<xsl:template match="faze2den" mode="faze3">
	<faze3den>
		<xsl:apply-templates mode="faze3" />
	</faze3den>
</xsl:template>

<!-- Zkopírovat předměty, ale odstranit semestr a den, tyto údaje jsou již
	 posunuty o několik úrovní výše. Navíc doplněn element colspan. -->
<xsl:template match="predmet" mode="faze3">
	<xsl:copy>
		<xsl:copy-of select="nazev" />	
		<xsl:copy-of select="typ" />	
		<xsl:copy-of select="zacatek" />	
		<xsl:copy-of select="konec" />	
		<xsl:copy-of select="budova" />	
		<xsl:copy-of select="mistnost" />	
		<colspan>
			<!-- Počet hodin předmětu N je řešením rovnice:
				 konec - zacatek = N * 45 + (N - 1) * 10 -->
			<xsl:variable name="rozdilCasuPlus10" 
				select="xs:time(konec) 
					- xs:time(zacatek) 
					+ xs:dayTimeDuration('PT10M')" />
			<xsl:value-of select=
		"(60 * fn:hours-from-duration($rozdilCasuPlus10)
			+ fn:minutes-from-duration($rozdilCasuPlus10))
			div 55" />
		</colspan>
	</xsl:copy>
</xsl:template>

<xsl:template match="faze2radka" mode="faze3">
	<xsl:variable name="predmety" select="predmet" />
	<faze3radka>
		<!-- Vyplnit prázdné hodiny prázdnými předměty. -->
		<xsl:for-each select="$data/data/casy/hodina">
			<xsl:if test="count(
							$predmety[zacatek &lt;= current()/zacatek
								and konec &gt;= current()/konec]
								) = 0">
				<predmet>
					<xsl:copy-of select="zacatek" />
					<xsl:copy-of select="konec" />
				</predmet>
			</xsl:if>
		</xsl:for-each>

		<!-- Doplnit neprázdné hodiny. -->
		<xsl:apply-templates select="predmet" mode="faze3" />
	</faze3radka>
</xsl:template>



<xsl:template match="/" mode="faze4">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css" />
	</head>
	<body>
		<xsl:call-template name="vytvoritRozvrh">
			<xsl:with-param name="semestr" select="'ZS'" />
		</xsl:call-template>
		<hr />
		<xsl:call-template name="vytvoritRozvrh">
			<xsl:with-param name="semestr" select="'LS'" />
		</xsl:call-template>
	</body>
</html>
</xsl:template>

<xsl:template name="vytvoritRozvrh">
	<xsl:param name="semestr" />
	<xsl:variable name="rozvrh" 
				select="$vysledekFaze3/rozvrh/semestr[zimniLetni = $semestr]" />
	
	<table class="tabulka">
		<tr>
			<th></th>
			<xsl:for-each select="$data/data/casy/hodina">
				<td><xsl:value-of select="@cislo" /></td>
			</xsl:for-each>
		</tr>
		<xsl:for-each select="$rozvrh/faze3den">
			<xsl:variable name="den" select="." />
			<xsl:for-each select="faze3radka">
				<tr>
					<!-- Vytvořit první sloupec - názvy dnů. -->
					<xsl:if test="position() = 1">
						<th>	
							<xsl:if test="$den/rowspan > 1">
								<xsl:attribute name="rowspan" 
												select="$den/rowspan" />
							</xsl:if>
							<xsl:value-of select="$den/den" />
						</th>
					</xsl:if>

					<xsl:apply-templates select="predmet" mode="faze4">
						<xsl:sort select="zacatek" />
					</xsl:apply-templates>
				</tr>
			</xsl:for-each>
		</xsl:for-each>
	</table>
</xsl:template>

<xsl:template match="predmet" mode="faze4">
	<td>
		<!-- Neprázdný předmět pokud existuje název. -->
		<xsl:if test="count(nazev) > 0">
			<xsl:if test="colspan > 1">
				<xsl:attribute name="colspan" 
								select="colspan" />
			</xsl:if>
			<xsl:choose>
				<xsl:when test="typ = 'Př'">
					<xsl:attribute name="class" select="'prednaska'" />
				</xsl:when>
				<xsl:when test="typ = 'Cv'">
					<xsl:attribute name="class" select="'cviceni'" />
				</xsl:when>
				<xsl:when test="typ = 'Se'">
					<xsl:attribute name="class" select="'seminar'" />
				</xsl:when>
				<xsl:otherwise />
			</xsl:choose>
			<xsl:value-of select="nazev" />
		</xsl:if>
	</td>
</xsl:template>




<xsl:variable name="vysledekFaze1">
	<xsl:apply-templates select="/" mode="faze1" />
</xsl:variable>

<!-- Fáze 2: rozdělit dny do jednotlivých řádků podle počtu kolizí. -->
<xsl:variable name="vysledekFaze2">
	<xsl:apply-templates select="$vysledekFaze1" mode="faze2" />
</xsl:variable>

<!-- Fáze 3: pro každý řádek každého dne doplnit prázdné předměty pro prázdné
			 hodiny. -->
<xsl:variable name="vysledekFaze3">
	<xsl:apply-templates select="$vysledekFaze2" mode="faze3" />
</xsl:variable>

<!-- Fáze 4: vytvoření html rozvrhu. -->
<xsl:variable name="vysledekFaze4">
	<xsl:apply-templates select="$vysledekFaze3" mode="faze4" />
</xsl:variable>

<xsl:template match="/">
	<xsl:apply-templates select="$vysledekFaze4" mode="faze4" />
</xsl:template>
</xsl:stylesheet>
