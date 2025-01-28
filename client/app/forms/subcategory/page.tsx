import { env } from "process";
import SubcategoryForm from "./subcategoryForm";

export default async function FormPage() {
  const data = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/category`,
    { cache: "force-cache" }
  );
  const categoryData: Category[] = await data.json();

  return (
    <div>
      <SubcategoryForm formData={categoryData} />
    </div>
  );
}
