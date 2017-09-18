package br.com.inmetrics.ssh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3FileAttributes;
import ch.ethz.ssh2.SFTPv3FileHandle;

public class SftpHelper extends RemoteConnection {

	public String getFileFromServer(String hostname, String pathArquivoOrigem, String pathDiretorioDestino) {

		try {

			if ( getConnection()== null ) {
				createConnection(hostname);
			}

			File fileOrigem = new File(pathArquivoOrigem);
			File dirDestino = new File(pathDiretorioDestino);

			if (dirDestino.exists() && dirDestino.isDirectory()) {
				dirDestino = new File(dirDestino, fileOrigem.getName());
			}

			SFTPv3Client sftp = new SFTPv3Client(connection);
			SFTPv3FileHandle file = sftp.openFileRO(pathArquivoOrigem);

			long fileOffset = 0;
			byte[] dst = new byte[32768];
			int i = 0;

			FileOutputStream output = new FileOutputStream(dirDestino);

			while ((i = sftp.read(file, fileOffset, dst, 0, dst.length)) != -1) {
				fileOffset += i;
				output.write(dst, 0, i);
			}

			output.close();
			sftp.closeFile(file);
			sftp.close();

			return "O arquivo ${fileOrigem.getCanonicalFile()} foi copiado com sucesso!";
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public String sendFileToServer(String hostname, String pathArquivoOrigem, String pathDiretorioDestino) throws IOException {

		try {
			File rfile = new File(pathDiretorioDestino);
			File lfile = new File(pathArquivoOrigem);

			if (!lfile.exists() || lfile.isDirectory()) {
				throw new IOException("Arquivo local deve ser um arquivo regular: " + pathArquivoOrigem);
			}

			if (super.getConnection() == null) {
				createConnection(hostname);
			}

			SFTPv3Client sftp = new SFTPv3Client(connection);

			SFTPv3FileAttributes attr = sftp.lstat(pathDiretorioDestino);

			if (attr.isDirectory()) {
				rfile = new File(pathDiretorioDestino, lfile.getName());
			}

			SFTPv3FileHandle file = sftp.createFileTruncate(rfile.getCanonicalPath());

			long fileOffset = 0;
			byte[] src = new byte[32768];
			int i = 0;

			FileInputStream input = new FileInputStream(lfile);

			while ((i = input.read(src)) != -1) {
				sftp.write(file, fileOffset, src, 0, i);
				fileOffset += i;
			}

			input.close();
			sftp.closeFile(file);
			sftp.close();

			return "O arquivo ${rfile.getCanonicalPath()} foi enviado com sucesso!";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
}